package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * [Interception] that may react to a received [Action] by either ignoring it or consuming/forwarding it and executing an asynchronous side effect with
 * continuous updates.
 *
 * The [react] block may return one of the following:
 * * [LiveReaction.Ignoring] - Simply forwards the `action`.
 * * [LiveReaction.Consuming] - Immediately returns its `immediateAction` and passes the `action` to its suspending `block`.
 * * [LiveReaction.Forwarding] - Similar to `Consuming` but forwards the `action` instead of returning.
 *
 * The `flow` block is called asynchronously and the returned [Flow] is collected. To update state call [FlowCollector.emit] in the returned flow.
 * The flow is collected in the scope of the corresponding [EiffelViewModel]'s [CoroutineScope], so it will be cancelled when [EiffelViewModel.onCleared]
 * is called during execution. Since cancellation is cooperative in Coroutines, if the side effect wants to support it the easiest way is to use [flow]
 * builder and check for [isActive].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[debugName] Custom name to use when logging the [LiveCommand] in debug mode.
 * @param[react] Lambda expression called with the received [Action]. Use one of [LiveReactionScope.consuming], [LiveReactionScope.forwarding]
 * or [LiveReactionScope.ignoring].
 */
@UseExperimental(FlowPreview::class)
class LiveCommand<S : State, A : Action>(debugName: String = "", private val react: LiveReactionScope.(action: A) -> LiveReaction<S, A>) : Interception<S, A> {

    override val debugName: String = debugName.ifEmpty { toString() }

    @UseExperimental(ObsoleteCoroutinesApi::class)
    override suspend fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (action: A) -> Unit, next: Next<S, A>): A? {
        return when (val reaction = LiveReactionScope.react(action)) {
            is LiveReaction.Consuming -> {
                scope.launch {
                    reaction.flow(state).collect { dispatch(it) }
                }
                reaction.immediateAction
            }
            is LiveReaction.Forwarding -> {
                scope.launch {
                    reaction.flow(state).collect { dispatch(it) }
                }
                next(scope, state, action, dispatch)
            }
            is LiveReaction.Ignoring -> next(scope, state, action, dispatch)
        }
    }
}
