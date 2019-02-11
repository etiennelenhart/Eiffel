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
 */
abstract class Pipe<S : State, A : Action> : Interception<S, A> {

    /**
     * Called with the current [State] and received [Action].
     */
    protected abstract fun before(state: S, action: A)

    /**
     * Called with the current [State] and updated [Action] from next items. Might be 'null' if blocked by a following [Filter].
     */
    protected abstract fun after(state: S, action: A?)

    final override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (A) -> Unit, next: Next<S, A>): A? {
        before(state, action)
        val newAction = next(scope, state, action, dispatch)
        after(state, newAction)
        return newAction
    }
}

/**
 * Convenience builder function that returns an object extending [Pipe]. Passes provided lambdas to overridden functions.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [Pipe] in debug mode.
 * @param[before] Lambda expression called with the current [State] and received [Action].
 * @param[after] Lambda expression called with the current [State] and updated [Action] from next item. Might be 'null' if blocked by a following [Filter].
 * @return An object extending [Pipe].
 */
fun <S : State, A : Action> pipe(
    debugName: String = "",
    before: (state: S, action: A) -> Unit = { _, _ -> },
    after: (state: S, action: A?) -> Unit = { _, _ -> }
): Pipe<S, A> {
    return object : Pipe<S, A>() {
        override val debugName: String = debugName.ifEmpty { toString() }

        override fun before(state: S, action: A) = before(state, action)

        override fun after(state: S, action: A?) = after(state, action)
    }
}
