package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class PipeTest {

    object TestState : State
    sealed class TestAction : Action {
        object First : TestAction()
        object Second : TestAction()
    }

    @Test
    fun `GIVEN Pipe WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.First
        val pipe = Pipe<TestState, TestAction> { _, _ -> }

        val actual = pipe(this, TestState, expected, {}, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Pipe WHEN invoked THEN 'before' is called before 'next'`() = runBlocking {
        var actual = ""
        val pipe = Pipe<TestState, TestAction>(before = { _, _ -> actual = "before" })

        pipe(this, TestState, TestAction.First, {}, { _, _, action, _ -> if (actual.isEmpty()) actual = "next"; action })

        assertEquals("before", actual)
    }

    @Test
    fun `GIVEN Pipe WHEN invoked THEN 'after' is called after 'next'`() = runBlocking {
        var actual = ""
        val pipe = Pipe<TestState, TestAction> { _, _ -> actual = "after" }

        pipe(this, TestState, TestAction.First, {}, { _, _, action, _ -> actual = "next"; action })

        assertEquals("after", actual)
    }

    @Test
    fun `GIVEN Pipe WHEN invoked THEN 'before' is called with 'action'`() = runBlocking {
        val expected = TestAction.First
        var actual: TestAction? = null
        val pipe = Pipe<TestState, TestAction>(before = { _, action -> actual = action })

        pipe(this, TestState, expected, {}, { _, _, _, _ -> TestAction.Second })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Pipe WHEN invoked THEN 'after' is called with action returned from 'next'`() = runBlocking {
        val expected = TestAction.Second
        var actual: TestAction? = null
        val pipe = Pipe<TestState, TestAction> { _, action -> actual = action }

        pipe(this, TestState, TestAction.First, {}, { _, _, _, _ -> expected })

        assertEquals(expected, actual)
    }
}
