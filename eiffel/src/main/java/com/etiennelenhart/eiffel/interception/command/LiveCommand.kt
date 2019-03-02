package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * [Interception] that may react to a received [Action] by either ignoring it or consuming it and executing an asynchronous side effect with continuous
 * updates. If consuming provide [LiveReaction.Consuming] with an `immediateAction` that is immediately returned to not interrupt state updating and a
 * suspending `block` returning a [Channel]. In the `block` call [Channel.send] which will dispatch the provided [Action] using [EiffelViewModel.dispatch],
 * effectively invoking the [EiffelViewModel.interceptions] chain again.
 *
 * *Note: The suspending block is scoped to the corresponding [EiffelViewModel]'s lifecycle.*
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
abstract class LiveCommand<S : State, A : Action> : Interception<S, A> {

    /**
     * Return one of the following:
     * * [Reaction.Ignoring] - Simply forwards the [action].
     * * [Reaction.Consuming] - Immediately returns its `immediateAction` and passes the [action] to its suspending `block`.
     * * [Reaction.Forwarding] - Similar to `Consuming` but forwards the [action] instead of returning.
     *
     * The suspending `block` is called asynchronously and the returned channel is subscribed to. To update state from the `block` call [Channel.send]
     * on the returned channel. The block is scoped to the corresponding [EiffelViewModel]'s [CoroutineScope], so it will be cancelled when
     * [EiffelViewModel.onCleared] is called during execution. Since cancellation is cooperative with coroutines, if the side effect wants to support
     * it the easiest way is to use [produce] builder and check for [isActive].
     *
     * @param[action] The received [Action].
     * @return Either [LiveReaction.Consuming] or [LiveReaction.Ignoring].
     */
    protected abstract fun react(action: A): LiveReaction<S, A>

    @UseExperimental(ObsoleteCoroutinesApi::class)
    final override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (action: A) -> Unit, next: Next<S, A>): A? {
        return when (val reaction = react(action)) {
            is LiveReaction.Consuming -> {
                scope.launch {
                    val channel = reaction.block(scope, state)
                    channel.consumeEach(dispatch)
                }
                reaction.immediateAction
            }
            is LiveReaction.Forwarding -> {
                scope.launch {
                    val channel = reaction.block(scope, state)
                    channel.consumeEach(dispatch)
                }
                next(scope, state, action, dispatch)
            }
            is LiveReaction.Ignoring -> next(scope, state, action, dispatch)
        }
    }
}

/**
 * /**
 * Convenience builder function that returns an object extending [LiveCommand]. Passes provided lambda to overridden function.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [LiveCommand] in debug mode.
 * @param[react] Lambda expression called with the received [Action]. Use one of [LiveReactionScope.consuming], [LiveReactionScope.forwarding]
 * or [LiveReactionScope.ignoring]. (see [LiveCommand.react])
 * @return An object extending [LiveCommand].
*/
 */
fun <S : State, A : Action> liveCommand(
    debugName: String = "",
    react: LiveReactionScope.(action: A) -> LiveReaction<S, A>
): LiveCommand<S, A> {
    return object : LiveCommand<S, A>() {
        override val debugName: String = debugName.ifEmpty { toString() }

        override fun react(action: A) = LiveReactionScope.react(action)
    }
}

/**
 * Only used to scope [LiveReaction] builder functions.
 */
object LiveReactionScope
