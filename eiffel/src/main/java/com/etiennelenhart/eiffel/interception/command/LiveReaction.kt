package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.command.LiveReaction.*
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Reaction of a [LiveCommand] to a received [Action]. [Consuming] indicates the intent to consume the [Action], [Ignoring] to simply forward it.
 *
 * @param[S] Type of [State] to receive if consuming.
 * @param[A] Type of [Action] to react to.
 */
sealed class LiveReaction<S : State, A : Action> {

    /**
     * Variant of [LiveReaction] indicating the intent to consume the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to immediately return and continuously send.
     * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
     * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    class Consuming<S : State, A : Action>(
        val immediateAction: A,
        val block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>
    ) : LiveReaction<S, A>()

    /**
     * Variant of [LiveReaction] indicating the intent to consume and forward the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to react to and continuously send.
     * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    class Forwarding<S : State, A : Action>(val block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>) : LiveReaction<S, A>()

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
 * Convenience builder function for the [Consuming] variant of [LiveReaction].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of [Action] to immediately return and continuously send.
 * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
 * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
 * @return Instance of [Consuming] variant.
 */
fun <S : State, A : Action> liveConsuming(immediateAction: A, block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>) =
    LiveReaction.Consuming(immediateAction, block)

/**
 * Convenience builder function for the [Forwarding] variant of [LiveReaction].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of [Action] to react to and continuously send.
 * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
 * @return Instance of [Forwarding] variant.
 */
fun <S : State, A : Action> liveForwarding(block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>) = LiveReaction.Forwarding(block)

/**
 * Convenience builder function for the [Ignoring] variant of [LiveReaction].
 *
 * @param[S] Type of [State] to receive if consuming.
 * @param[A] Type of [Action] to react to.
 * @return Instance of [Ignoring] variant.
 */
fun <S : State, A : Action> liveIgnoring() = LiveReaction.Ignoring<S, A>()
