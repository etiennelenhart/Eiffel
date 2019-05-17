package com.etiennelenhart.eiffel.interception.command

import com.etiennelenhart.eiffel.interception.command.Reaction.*
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State

/**
 * Reaction of a [Command] to a received [Action]. [Consuming] indicates the intent to consume the [Action],
 * [Forwarding] to consume and forward it and [Ignoring] to simply forward it.
 *
 * @param[S] Type of [State] to receive if consuming.
 * @param[A] Type of [Action] to react to.
 */
sealed class Reaction<S : State, A : Action> {

    /**
     * Variant of [Reaction] indicating the intent to consume the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to immediately return and dispatch asynchronously.
     * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
     * @param[block] Suspending lambda expression that is called asynchronously. To update state when done call provided `dispatch` lambda.
     */
    class Consuming<S : State, A : Action>(
        val immediateAction: A,
        val block: suspend (state: S, dispatch: (A) -> Unit) -> Unit
    ) : Reaction<S, A>()

    /**
     * Variant of [Reaction] indicating the intent to consume and forward the received [Action].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to react to and dispatch asynchronously.
     * @param[block] Suspending lambda expression that is called asynchronously. To update state when done call provided `dispatch` lambda.
     */
    class Forwarding<S : State, A : Action>(val block: suspend (state: S, dispatch: (A) -> Unit) -> Unit) : Reaction<S, A>()

    /**
     * Variant of [Reaction] indicating that the [Action] is ignored and should be forwarded.
     *
     * *Note: Type parameters are required since they occur in invariant positions in [Consuming].*
     *
     * @param[S] Type of [State] to receive if consuming.
     * @param[A] Type of [Action] to react to.
     */
    class Ignoring<S : State, A : Action> : Reaction<S, A>()
}

/**
 * Scope for [Reaction] builder functions.
 */
object ReactionScope {

    /**
     * Convenience builder function for the [Consuming] variant of [Reaction].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to immediately return and dispatch asynchronously.
     * @param[immediateAction] [Action] to return immediately so state updating is not interrupted, e.g. indicating a 'pending' operation.
     * @param[block] Suspending lambda expression that is called asynchronously. To update state when done call provided `dispatch` lambda.
     * @return Instance of [Consuming] variant.
     */
    fun <S : State, A : Action> consuming(immediateAction: A, block: suspend (state: S, dispatch: (A) -> Unit) -> Unit) = Consuming(immediateAction, block)

    /**
     * Convenience builder function for the [Forwarding] variant of [Reaction].
     *
     * @param[S] Type of [State] to receive.
     * @param[A] Type of [Action] to react to and dispatch asynchronously.
     * @param[block] Suspending lambda expression that is called asynchronously. To update state when done call provided `dispatch` lambda.
     * @return Instance of [Forwarding] variant.
     */
    fun <S : State, A : Action> forwarding(block: suspend (state: S, dispatch: (A) -> Unit) -> Unit) = Forwarding(block)

    /**
     * Convenience builder function for the [Ignoring] variant of [Reaction].
     *
     * @param[S] Type of [State] to receive if consuming.
     * @param[A] Type of [Action] to react to.
     * @return Instance of [Ignoring] variant.
     */
    fun <S : State, A : Action> ignoring() = Ignoring<S, A>()
}
