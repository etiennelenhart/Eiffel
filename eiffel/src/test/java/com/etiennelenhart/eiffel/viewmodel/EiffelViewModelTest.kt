package com.etiennelenhart.eiffel.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class EiffelViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    data class TestState(val count: Int = 0) : State

    sealed class TestAction : Action {
        object Increment : TestAction()
        object Decrement : TestAction()
        class Add(val amount: Int) : TestAction()
    }

    object TestStateUpdate : Update<TestState, TestAction> {

        override fun invoke(state: TestState, action: TestAction): TestState {
            val count = state.count
            return when (action) {
                TestAction.Increment -> state.copy(count = count + 1)
                TestAction.Decrement -> state.copy(count = count - 1)
                is TestAction.Add -> state.copy(count = count + action.amount)
            }
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    class TestViewModel : EiffelViewModel<TestState, TestAction>(TestState(), TestStateUpdate, dispatcher = Dispatchers.Unconfined)

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' called THEN 'action' is processed`() {
        val viewModel = TestViewModel()

        var actual = 0
        viewModel.observeStateForever { actual = it.count }
        viewModel.dispatch(TestAction.Increment)

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' called multiple times THEN all 'actions' are processed`() {
        val viewModel = TestViewModel()

        var actual = 0
        viewModel.observeStateForever { actual = it.count }
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
        val viewModel = object : EiffelViewModel<TestState, TestAction>(TestState(), TestStateUpdate, dispatcher = Dispatchers.Unconfined) {
            init {
                addStateSource(OneLiveData()) { TestAction.Add(it) }
            }
        }

        var actual = 0
        viewModel.observeStateForever { actual = it.count }

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass with a removed source 'LiveData' WHEN 'observeStateForever' called THEN initial state is emitted`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel = object : EiffelViewModel<TestState, TestAction>(TestState(), TestStateUpdate, dispatcher = Dispatchers.Unconfined) {
            init {
                val source = OneLiveData()
                addStateSource(source) { TestAction.Add(it) }
                removeStateSource(source)
            }
        }

        var actual = 0
        viewModel.observeStateForever { actual = it.count }

        assertEquals(0, actual)
    }
}
