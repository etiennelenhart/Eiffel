package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class FilterTest {

    object TestState : State
    sealed class TestAction : Action {
        object Allow : TestAction()
        object Block : TestAction()
        object Next : TestAction()
    }

    @Test
    fun `GIVEN Filter with passing 'predicate' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Next
        val filter = filter<TestState, TestAction> { _, action -> action is TestAction.Allow }

        val actual = filter(this, TestState, TestAction.Allow, {}, { _, _, _, _ -> expected })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Filter with blocking 'predicate' WHEN invoked with 'action' THEN 'action' is blocked`() = runBlocking {
        val expected = TestAction.Block
        val filter = filter<TestState, TestAction> { _, action -> action is TestAction.Allow }

        val actual = filter(this, TestState, TestAction.Block, {}, { _, _, _, _ -> TestAction.Next })

        assertEquals(expected, actual)
    }
}
