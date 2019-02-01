package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class CommandTest {

    object TestState : State
    sealed class TestAction : Action {
        object Increment : TestAction()
        object Decrement : TestAction()
        object Loading : TestAction()
        class Add(val amount: Int) : TestAction()
    }

    @Test
    fun `GIVEN Command with consuming 'react' WHEN invoked with 'action' THEN 'immediateAction' is forwarded`() = runBlocking {
        val expected = TestAction.Loading
        val command = command<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> consuming(expected) { _, _, _ -> delay(20) }
                else -> ignoring()
            }
        }

        val actual = command(this, TestState, TestAction.Increment, { }, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Command with consuming 'react' WHEN invoked with 'action' THEN 'dispatch' can be called in 'block'`() = runBlocking {
        val expected = TestAction.Add(1)
        val command = command<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> consuming(TestAction.Loading) { _, _, dispatch ->
                    delay(40)
                    dispatch(expected)
                }
                else -> ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, TestAction.Increment, { actual = it }, { _, _, action, _ -> action })

        delay(80)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Command with ignoring 'react' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Decrement
        val command = command<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> consuming(TestAction.Loading) { _, _, _ -> delay(20) }
                else -> ignoring()
            }
        }

        val actual = command(this, TestState, expected, { }, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }
}
