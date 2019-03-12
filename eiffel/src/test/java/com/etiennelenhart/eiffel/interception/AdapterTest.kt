package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class AdapterTest {

    object TestState : State
    sealed class TestAction : Action {
        object First : TestAction()
        object Second : TestAction()
    }

    @Test
    fun `GIVEN Adapter WHEN invoked with 'action' THEN adapted 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Second
        val adapter = adapter<TestState, TestAction> { _, _ -> expected }

        val actual = adapter(this, TestState, TestAction.First, {}, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }
}
