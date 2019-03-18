package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope

/**
 * [Interception] that invokes the next item with the [Action] returned from [adapt].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [Adapter] in debug mode.
 * @param[adapt] Lambda expression called with the current [State] and received [Action]. May adapt the action to a new one.
 */
class Adapter<S : State, A : Action>(debugName: String = "", private val adapt: (state: S, action: A) -> A) : Interception<S, A> {

    override val debugName = debugName.ifEmpty { toString() }

    override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (action: A) -> Unit, next: Next<S, A>): A? {
        return next(scope, state, adapt(state, action), dispatch)
    }
}
