package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

@UseExperimental(FlowPreview::class)
class LiveCommandTest {

    object TestState : State
    sealed class TestAction : Action {
        object Increment : TestAction()
        object Decrement : TestAction()
        object Loading : TestAction()
        class Add(val amount: Int) : TestAction()
        object Dummy : TestAction()
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN 'immediateAction' is returned`() = runBlocking {
        val expected = TestAction.Loading
        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Consuming(expected) { emptyFlow() }
                else -> LiveReaction.Ignoring()
            }
        }

        val actual = command(this, TestState, TestAction.Increment, { }, { _, _, _, _ -> TestAction.Dummy })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN calling 'send' on channel causes dispatch`() = runBlocking {
        val emitted = CompletableDeferred<Unit>()

        val expected = TestAction.Add(1)
        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Consuming(TestAction.Loading) {
                    flow {
                        emit(expected)
                        emitted.complete(Unit)
                    }
                }
                else -> LiveReaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, TestAction.Increment, { actual = it }, { _, _, _, _ -> TestAction.Dummy })

        emitted.await()
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with consuming 'react' WHEN invoked with 'action' THEN multiple 'send' calls on channel cause multiples dispatches`() = runBlocking {
        val emitted = CompletableDeferred<Unit>()

        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Consuming(TestAction.Loading) {
                    flow {
                        emit(TestAction.Add(1))
                        emit(TestAction.Add(1))
                        emitted.complete(Unit)
                    }
                }
                else -> LiveReaction.Ignoring()
            }
        }

        var actual = 0
        command(this, TestState, TestAction.Increment, { actual++ }, { _, _, _, _ -> TestAction.Dummy })

        emitted.await()
        assertEquals(2, actual)
    }

    @Test
    fun `GIVEN LiveCommand with forwarding 'react' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Loading
        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Forwarding { emptyFlow() }
                else -> LiveReaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, expected, { }, { _, _, action, _ -> action.also { actual = it } })

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with forwarding 'react' WHEN invoked with 'action' THEN calling 'send' on channel causes dispatch`() = runBlocking {
        val emitted = CompletableDeferred<Unit>()

        val expected = TestAction.Add(1)
        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Forwarding {
                    flow {
                        emit(expected)
                        emitted.complete(Unit)
                    }
                }
                else -> LiveReaction.Ignoring()
            }
        }

        var actual: TestAction? = null
        command(this, TestState, TestAction.Increment, { actual = it }, { _, _, _, _ -> TestAction.Dummy })

        emitted.await()
        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveCommand with forwarding 'react' WHEN invoked with 'action' THEN multiple 'send' calls on channel cause multiples dispatches`() =
        runBlocking {
            val emitted = CompletableDeferred<Unit>()

            val command = LiveCommand<TestState, TestAction> { action ->
                when (action) {
                    TestAction.Increment -> LiveReaction.Forwarding {
                        flow {
                            emit(TestAction.Add(1))
                            emit(TestAction.Add(1))
                            emitted.complete(Unit)
                        }
                    }
                    else -> LiveReaction.Ignoring()
                }
            }

            var actual = 0
            command(this, TestState, TestAction.Increment, { actual++ }, { _, _, _, _ -> TestAction.Dummy })

            emitted.await()
            assertEquals(2, actual)
        }

    @Test
    fun `GIVEN LiveCommand with ignoring 'react' WHEN invoked with 'action' THEN 'action' is forwarded`() = runBlocking {
        val expected = TestAction.Decrement
        val command = LiveCommand<TestState, TestAction> { action ->
            when (action) {
                TestAction.Increment -> LiveReaction.Consuming(TestAction.Loading) { emptyFlow() }
                else -> LiveReaction.Ignoring()
            }
        }

        val actual = command(this, TestState, expected, { }, { _, _, action, _ -> action })

        assertEquals(expected, actual)
    }
}
