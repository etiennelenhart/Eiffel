package com.etiennelenhart.eiffel.test

import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Interceptions
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.runBlocking

/**
 * Allows testing a chain of [Interceptions] without an instance of [EiffelViewModel].
 *
 * @param[S] Type of [State] to test with.
 * @param[A] Type of [Action] to test with.
 * @param[interceptions] Chain of [Interception] instances to test.
 * @param[currentState] Current [State] to pass to the chain.
 * @param[action] [Action] to be processed by the chain.
 * @return [Action] resulting from processing, may be null.
 */
fun <S : State, A : Action> testInterceptions(interceptions: Interceptions<S, A>, currentState: S, action: A) = runBlocking {
    fun next(index: Int): Next<S, A> = if (index == interceptions.size) {
        { _, _, action, _ -> action }
    } else {
        { scope, state, action, dispatch ->
            interceptions[index].run {
                invoke(scope, state, action, dispatch, next(index + 1))
            }
        }
    }

    next(0).invoke(this, currentState, action) {}
}
