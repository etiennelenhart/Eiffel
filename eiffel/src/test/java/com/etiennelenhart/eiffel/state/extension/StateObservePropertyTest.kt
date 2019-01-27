package com.etiennelenhart.eiffel.state.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.update
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@UseExperimental(ExperimentalCoroutinesApi::class)
class StateObservePropertyTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @BeforeTest
    fun before() = Dispatchers.setMain(Dispatchers.Unconfined)

    @AfterTest
    fun after() = Dispatchers.resetMain()

    data class TestState(val count: Int = 0, val other: String = "") : State

    sealed class TestAction : Action {
        object Increment : TestAction()
        object Other : TestAction()
    }

    val testStateUpdate = update<TestState, TestAction> { state, action ->
        val count = state.count
        when (action) {
            TestAction.Increment -> state.copy(count = count + 1)
            TestAction.Other -> state.copy(other = "changed")
        }
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' changes observed property THEN 'onChange' is invoked`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {}

        var actual = 0
        viewModel.state.observePropertyForever({ it.count }) { actual = it }
        viewModel.dispatch(TestAction.Increment)

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' does not change observed property THEN 'onChange' is not invoked`() {
        @UseExperimental(ExperimentalCoroutinesApi::class)
        val viewModel =
            object : EiffelViewModel<TestState, TestAction>(TestState(), testStateUpdate, emptyList(), Dispatchers.Unconfined, Dispatchers.Unconfined) {}

        var actual = 0
        viewModel.state.observePropertyForever({ it.count }) { actual = it }
        viewModel.dispatch(TestAction.Other)

        assertEquals(0, actual)
    }
}
