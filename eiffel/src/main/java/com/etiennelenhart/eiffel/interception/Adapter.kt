package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope

/**
 * [Interception] that invokes the next item with the [Action] returned from [adapt].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
abstract class Adapter<S : State, A : Action> : Interception<S, A> {

    /**
     * Adapts the given [action] to a new one.
     *
     * @param[action] Received [Action].
     * @return The adapted [Action].
     */
    protected abstract fun adapt(action: A): A

    final override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (action: A) -> Unit, next: Next<S, A>): A {
        return next(scope, state, adapt(action), dispatch)
    }
}

/**
 * Convenience builder function that returns an object extending [Adapter]. Passes provided lambda to overridden function.
 *
 * @param[adapt] Lambda expression that adapts the given [Action] to a new one.
 * @return An object extending [Adapter].
 */
fun <S : State, A : Action> adapter(adapt: (action: A) -> A): Adapter<S, A> {
    return object : Adapter<S, A>() {
        override fun adapt(action: A) = adapt(action)
    }
}
