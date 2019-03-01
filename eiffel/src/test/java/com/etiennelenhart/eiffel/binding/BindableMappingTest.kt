package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State
import org.junit.Test
import kotlin.test.assertTrue

class BindableMappingTest {

    data class TestState(val count: Int = 0) : State

    data class BindableTestState(val emptyStateVisible: Boolean = false) : BindableState

    @Test
    fun `GIVEN BindableMapping with 'initialState' WHEN invoked with current value THEN current value is transformed with 'map'`() {
        val mapping = bindableMapping<TestState, BindableTestState>(BindableTestState()) { state ->
            copy(emptyStateVisible = this.emptyStateVisible && state.count == 0)
        }

        val actual = mapping(TestState(), BindableTestState(true)).emptyStateVisible

        assertTrue(actual)
    }
}
