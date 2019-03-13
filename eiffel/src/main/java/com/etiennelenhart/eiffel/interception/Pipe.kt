package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope

/**
 * [Interception] that calls [before] with the received [Action] and [after] with the updated [Action] returned from next chain item.
 * Provides current state to both and forwards received [Action] to next item.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [Pipe] in debug mode.
 * @param[before] Lambda expression called with the current [State] and received [Action].
 * @param[after] Lambda expression called with the current [State] and updated [Action] from next item. Might be 'null' if blocked by a [Filter].
 */
class Pipe<S : State, A : Action>(
    debugName: String = "",
    private val before: (state: S, action: A) -> Unit = { _, _ -> },
    private val after: (state: S, action: A?) -> Unit = { _, _ -> }
) : Interception<S, A> {

    override val debugName: String = debugName.ifEmpty { toString() }

    override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (A) -> Unit, next: Next<S, A>): A? {
        before(state, action)
        val newAction = next(scope, state, action, dispatch)
        after(state, newAction)
        return newAction
    }
}
