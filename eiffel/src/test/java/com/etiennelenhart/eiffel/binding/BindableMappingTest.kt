package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State
import org.junit.Test
import kotlin.test.assertTrue

class BindableMappingTest {

    data class FirstTestState(val count: Int = 0) : State
    data class SecondTestState(val count: Int = 0) : State
    data class ThirdTestState(val count: Int = 0) : State

    data class BindableTestState(val emptyStateVisible: Boolean = false) : BindableState

    @Test
    fun `GIVEN BindableMapping1 WHEN invoked with 'state' THEN 'state' is transformed with 'map'`() {
        val mapping = bindableMapping<FirstTestState, BindableTestState> { state ->
            copy(emptyStateVisible = this.emptyStateVisible && state.count == 0)
        }

        val actual = mapping(FirstTestState(), BindableTestState(true)).emptyStateVisible

        assertTrue(actual)
    }

    @Test
    fun `GIVEN BindableMapping2 WHEN invoked with 'firstState' and 'secondState' THEN both are transformed with 'map'`() {
        val mapping = bindableMapping<FirstTestState, SecondTestState, BindableTestState> { firstState, secondState ->
            copy(emptyStateVisible = this.emptyStateVisible && firstState.count == 0 && secondState.count == 0)
        }

        val actual = mapping(FirstTestState(), SecondTestState(), BindableTestState(true)).emptyStateVisible

        assertTrue(actual)
    }

    @Test
    fun `GIVEN BindableMapping3 WHEN invoked with 'firstState', 'secondState' and 'thirdState' THEN all three are transformed with 'map'`() {
        val mapping = bindableMapping<FirstTestState, SecondTestState, ThirdTestState, BindableTestState> { firstState, secondState, thirdState ->
            copy(emptyStateVisible = this.emptyStateVisible && firstState.count == 0 && secondState.count == 0 && thirdState.count == 0)
        }

        val actual = mapping(FirstTestState(), SecondTestState(), ThirdTestState(), BindableTestState(true)).emptyStateVisible

        assertTrue(actual)
    }
}
