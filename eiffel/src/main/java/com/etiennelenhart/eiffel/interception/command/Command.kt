package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.*

/**
 * [Interception] that may react to a received [Action] by either ignoring it or consuming/forwarding it and executing an asynchronous side effect.
 *
 * The [react] block may return one of the following:
 * * [Reaction.Ignoring] - Simply forwards the `action`.
 * * [Reaction.Consuming] - Immediately returns its `immediateAction` and passes the `action` to its suspending `block`.
 * * [Reaction.Forwarding] - Similar to `Consuming` but forwards the `action` instead of returning it.
 *
 * The suspending `block` is called asynchronously and not awaited. To update state from the `block` call the provided `dispatch` lambda expression.
 * The block is scoped to the corresponding [EiffelViewModel]'s [CoroutineScope], so it will be cancelled when [EiffelViewModel.onCleared] is called
 * during execution. Since cancellation is cooperative with coroutines, if the side effect wants to support it either use a [coroutineScope] builder
 * and check for [isActive] or use [yield].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [Command] in debug mode.
 * @param[react] Lambda expression called with the received [Action]. Use either [ReactionScope.consuming], [ReactionScope.forwarding]
 * or [ReactionScope.ignoring].
 */
class Command<S : State, A : Action>(debugName: String = "", private val react: ReactionScope.(action: A) -> Reaction<S, A>) : Interception<S, A> {

    override val debugName: String = debugName.ifEmpty { toString() }

    override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (A) -> Unit, next: Next<S, A>): A? {
        return when (val reaction = ReactionScope.react(action)) {
            is Reaction.Consuming -> {
                scope.launch { reaction.block(state, dispatch) }
                reaction.immediateAction
            }
            is Reaction.Forwarding -> {
                scope.launch { reaction.block(state, dispatch) }
                next(scope, state, action, dispatch)
            }
            is Reaction.Ignoring -> next(scope, state, action, dispatch)
        }
    }
}
