package com.etiennelenhart.eiffel.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.Update
import com.etiennelenhart.eiffel.state.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ObsoleteCoroutinesApi
@UseExperimental(ExperimentalCoroutinesApi::class)
class EiffelViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @BeforeTest
    fun before() = Dispatchers.setMain(Dispatchers.Unconfined)

    @AfterTest
    fun after() = Dispatchers.resetMain()

    data class TestState(val count: Int = 0, val other: String = "") : State

    sealed class TestAction : Action {
        object Increment : TestAction()
        object Decrement : TestAction()
        class Add(val amount: Int) : TestAction()
        object Other : TestAction()
    }

    val testStateUpdate = update<TestState, TestAction> { state, action ->
        val count = state.count
        when (action) {
            TestAction.Increment -> state.copy(count = count + 1)
            TestAction.Decrement -> state.copy(count = count - 1)
            is TestAction.Add -> state.copy(count = count + action.amount)
            TestAction.Other -> state.copy(other = "changed")
        }
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' called THEN 'action' is processed`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {}

        var actual = 0
        viewModel.state.observeForever { actual = it.count }
        viewModel.dispatch(TestAction.Increment)

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' called multiple times THEN all 'actions' are processed`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {}

        var actual = 0
        viewModel.state.observeForever { actual = it.count }
        viewModel.dispatch(TestAction.Increment)
        viewModel.dispatch(TestAction.Increment)
        viewModel.dispatch(TestAction.Decrement)

        assertEquals(1, actual)
    }

    class OneLiveData : LiveData<Int>() {

        init {
            value = 1
        }
    }

    @Test
    fun `GIVEN EiffelViewModel subclass with a source 'LiveData' WHEN 'observeStateForever' called THEN updated state is emitted`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {
                init {
                    addStateSource(OneLiveData()) { TestAction.Add(it) }
                }
            }

        var actual = 0
        viewModel.state.observeForever { actual = it.count }

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass with a removed source 'LiveData' WHEN 'observeStateForever' called THEN initial state is emitted`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {
                init {
                    val source = OneLiveData()
                    addStateSource(source) { TestAction.Add(it) }
                    removeStateSource(source)
                }
            }

        var actual = 0
        viewModel.state.observeForever { actual = it.count }

        assertEquals(0, actual)
    }

    data class InterceptionState(val correct: Boolean = false) : State

    sealed class InterceptionAction : Action {
        object First : InterceptionAction()
        object Second : InterceptionAction()
        object Third : InterceptionAction()
    }

    object InterceptionStateUpdate : Update<InterceptionState, InterceptionAction> {
        override fun invoke(state: InterceptionState, action: InterceptionAction) = when (action) {
            InterceptionAction.Third -> state.copy(correct = true)
            else -> state
        }
    }

    val firstInterception = object : Interception<InterceptionState, InterceptionAction> {
        override suspend fun invoke(
            scope: CoroutineScope,
            state: InterceptionState,
            action: InterceptionAction,
            dispatch: (action: InterceptionAction) -> Unit,
            next: Next<InterceptionState, InterceptionAction>
        ): InterceptionAction {
            return if (action is InterceptionAction.First) {
                next(scope, state, InterceptionAction.Second, dispatch)
            } else {
                action
            }
        }
    }

    val secondInterception = object : Interception<InterceptionState, InterceptionAction> {
        override suspend fun invoke(
            scope: CoroutineScope,
            state: InterceptionState,
            action: InterceptionAction,
            dispatch: (action: InterceptionAction) -> Unit,
            next: Next<InterceptionState, InterceptionAction>
        ): InterceptionAction {
            return if (action is InterceptionAction.Second) {
                next(scope, state, InterceptionAction.Third, dispatch)
            } else {
                action
            }
        }
    }

    @Test
    fun `GIVEN EiffelViewModel subclass with interceptions WHEN 'dispatch' called THEN all interceptions are applied`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel = object : EiffelViewModel<InterceptionState, InterceptionAction>(
            InterceptionState(),
            InterceptionStateUpdate,
            listOf(firstInterception, secondInterception),
            Dispatchers.Unconfined,
            Dispatchers.Unconfined
        ) {}

        var actual = false
        viewModel.state.observeForever { actual = it.correct }
        viewModel.dispatch(InterceptionAction.First)

        assertTrue(actual)
    }
}
