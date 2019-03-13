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
        object Dummy : TestAction()
    }

    @Test
    fun `GIVEN Command with consuming 'react' WHEN invoked with 'action' THEN 'immediateAction' is returned`() = runBlocking {
        val expected = TestAction.Loading
        val command = Command<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> Reaction.Consuming(expected) { _, _ -> delay(20) }
                else -> Reaction.Ignoring()
            }
        }

        val actual = command(this, TestState, TestAction.Increment, { }, { _, _, _, _ -> TestAction.Dummy })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Command with consuming 'react' WHEN invoked with 'action' THEN 'dispatch' can be called in 'block'`() = runBlocking {
        val expected = TestAction.Add(1)
        val command = Command<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> Reaction.Consuming(TestAction.Loading) { _, dispatch ->
                    delay(40)
                    dispatch(expected)
                }
                else -> Reaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, TestAction.Increment, { actual = it }, { _, _, _, _ -> TestAction.Dummy })

        delay(80)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Command with forwarding 'react' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Loading
        val command = Command<TestState, TestAction> { action ->
            when (action) {
                TestAction.Loading -> Reaction.Forwarding { _, _ -> delay(20) }
                else -> Reaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, expected, { }, { _, _, action, _ -> action.also { actual = it } })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN Command with forwarding 'react' WHEN invoked with 'action' THEN 'dispatch' can be called in 'block'`() = runBlocking {
        val expected = TestAction.Add(1)
        val command = Command<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> Reaction.Forwarding { _, dispatch ->
                    delay(40)
                    dispatch(expected)
                }
                else -> Reaction.Ignoring()
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
        val command = Command<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> Reaction.Consuming(TestAction.Loading) { _, _ -> delay(20) }
                else -> Reaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, expected, { }, { _, _, action, _ -> action.also { actual = it } })

        assertEquals(expected, actual)
    }
}
