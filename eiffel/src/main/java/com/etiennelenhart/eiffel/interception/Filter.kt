package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope

/**
 * [Interception] that forwards a received [Action] if [predicate] is `true` or returns the [Action] if it's `false`.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
abstract class Filter<S : State, A : Action> : Interception<S, A> {

    /**
     * Lambda expression called with the current [State] and received [Action].
     *
     * @return `true` if received [Action] should be forwarded, `false` otherwise.
     */
    protected abstract val predicate: suspend (state: S, action: A) -> Boolean

    final override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (A) -> Unit, next: Next<S, A>): A {
        return if (predicate(state, action)) next(scope, state, action, dispatch) else action
    }
}

/**
 * Convenience builder function that returns an object extending [Filter]. Passes provided lambda to overridden property.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[predicate] Lambda expression called with the current [State] and received [Action]. Return `true` if received [Action] should be forwarded,
 * `false` otherwise.
 * @return An object extending [Filter].
 */
fun <S : State, A : Action> filter(predicate: suspend (state: S, action: A) -> Boolean): Filter<S, A> {
    return object : Filter<S, A>() {
        override val predicate = predicate
    }
}
