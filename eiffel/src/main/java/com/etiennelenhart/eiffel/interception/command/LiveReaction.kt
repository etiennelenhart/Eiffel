package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.command.LiveReaction.*
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

/**
 * Reaction of a [LiveCommand] to a received [Action]. [Consuming] indicates the intent to consume the [Action], [Ignoring] to simply forward it.
 *
 * @param[S] Type of [State] to receive if consuming.
 * @param[A] Type of [Action] to react to.
 */
@UseExperimental(FlowPreview::class)
sealed class LiveReaction<S : State, A : Action> {

    /**
     * Variant of [LiveReaction] indicating the intent to consume the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to immediately return and continuously send.
     * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
     * @param[flow] Lambda expression returning a [Flow] to collect. To update state call [FlowCollector.emit] on this flow.
     */
    class Consuming<S : State, A : Action>(val immediateAction: A, val flow: (state: S) -> Flow<A>) : LiveReaction<S, A>()

    /**
     * Variant of [LiveReaction] indicating the intent to consume and forward the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to react to and continuously send.
     * @param[flow] Lambda expression returning a [Flow] to collect. To update state call [FlowCollector.emit] on this flow.
     */
    class Forwarding<S : State, A : Action>(val flow: (state: S) -> Flow<A>) : LiveReaction<S, A>()

    /**
     * Variant of [LiveReaction] indicating that the [Action] is ignored and should be forwarded.
     *
     * *Note: Type parameters are required since they occur in invariant positions in [Consuming].*
     *
     * @param[S] Type of [State] to receive if consuming.
     * @param[A] Type of [Action] to react to.
     */
    class Ignoring<S : State, A : Action> : LiveReaction<S, A>()
}

/**
 * Scope for [LiveReaction] builder functions.
 */
@UseExperimental(FlowPreview::class)
object LiveReactionScope {

    /**
     * Convenience builder function for the [Consuming] variant of [LiveReaction].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to immediately return and continuously send.
     * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
     * @param[flow] Lambda expression returning a [Flow] to collect. To update state call [FlowCollector.emit] on this flow.
     * @return Instance of [Consuming] variant.
     */
    fun <S : State, A : Action> consuming(immediateAction: A, flow: (state: S) -> Flow<A>) = Consuming(immediateAction, flow)

    /**
     * Convenience builder function for the [Forwarding] variant of [LiveReaction].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to react to and continuously send.
     * @param[flow] Lambda expression returning a [Flow] to collect. To update state call [FlowCollector.emit] on this flow.
     * @return Instance of [Forwarding] variant.
     */
    fun <S : State, A : Action> forwarding(flow: (state: S) -> Flow<A>) = Forwarding(flow)

    /**
     * Convenience builder function for the [Ignoring] variant of [LiveReaction].
     *
     * @param[S] Type of [State] to receive if consuming.
     * @param[A] Type of [Action] to react to.
     * @return Instance of [Ignoring] variant.
     */
    fun <S : State, A : Action> ignoring() = Ignoring<S, A>()
}
