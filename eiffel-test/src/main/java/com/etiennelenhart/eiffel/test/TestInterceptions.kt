package com.etiennelenhart.eiffel.test

import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Interceptions
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.coroutineScope

/**
 * Allows testing a chain of [Interceptions] without an instance of [EiffelViewModel].
 *
 * @receiver Chain of [Interception] instances to test.
 * @param[S] Type of [State] to test with.
 * @param[A] Type of [Action] to test with.
 * @param[currentState] Current [State] to pass to the chain.
 * @param[action] [Action] to be processed by the chain.
 * @return [Action] resulting from processing, may be null.
 */
suspend fun <S : State, A : Action> Interceptions<S, A>.applyOn(currentState: S, action: A) = coroutineScope {
    val interceptions = this@applyOn

    fun next(index: Int): Next<S, A> {
        return if (index == interceptions.size) {
            { _, _, action, _ -> action }
        } else {
            { scope, state, action, dispatch ->
                interceptions[index].run {
                    invoke(scope, state, action, dispatch, next(index + 1))
                }
            }
        }
    }

    next(0).invoke(this, currentState, action) {}
}
