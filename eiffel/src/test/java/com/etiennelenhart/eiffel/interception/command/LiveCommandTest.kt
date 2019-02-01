package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@UseExperimental(ExperimentalCoroutinesApi::class)
class LiveCommandTest {

    object TestState : State
    sealed class TestAction : Action {
        object Increment : TestAction()
        object Decrement : TestAction()
        object Loading : TestAction()
        class Add(val amount: Int) : TestAction()
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN 'immediateAction' is forwarded`() = runBlocking {
        val expected = TestAction.Loading
        val command = liveCommand<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> liveConsuming(expected) { _, _ -> produce { delay(20) } }
                else -> liveIgnoring()
            }
        }

        val actual = command(this, TestState, TestAction.Increment, { }, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN calling 'send' on channel causes dispatch`() = runBlocking {
        val expected = TestAction.Add(1)
        val command = liveCommand<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> liveConsuming(TestAction.Loading) { _, _ ->
                    produce {
                        delay(40)
                        send(expected)
                        close()
                    }
                }
                else -> liveIgnoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, TestAction.Increment, { actual = it }, { _, _, action, _ -> action })

        delay(80)
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN multiple 'send' calls on channel cause multiples dispatches`() = runBlocking {
        val command = liveCommand<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> liveConsuming(TestAction.Loading) { _, _ ->
                    produce {
                        send(TestAction.Add(1))
                        delay(40)
                        send(TestAction.Add(1))
                        close()
                    }
                }
                else -> liveIgnoring()
            }
        }

        var actual = 0
        command(this, TestState, TestAction.Increment, { actual++ }, { _, _, action, _ -> action })

        delay(80)
        assertEquals(2, actual)
    }

    @Test
    fun `GIVEN LiveCommand with ignoring 'react' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Decrement
        val command = liveCommand<TestState, TestAction> {
            when (it) {
                TestAction.Increment -> liveConsuming(TestAction.Loading) { _, _ -> produce { delay(40) } }
                else -> liveIgnoring()
            }
        }

        val actual = command(this, TestState, expected, { }, { _, _, action, _ -> action })

        delay(80)
        assertEquals(expected, actual)
    }
}
