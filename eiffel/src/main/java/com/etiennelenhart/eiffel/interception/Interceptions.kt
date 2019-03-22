package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.interception.command.*
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

@DslMarker
annotation class InterceptionsBuilderMarker

/**
 * A chain of [Interception] instances to use for [EiffelViewModel.interceptions].
 *
 * See [interceptions] or [Interceptions.Builder] for a more convenient way to build the chain.
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[chain] List of [Interception] instances.
 */
class Interceptions<S : State, A : Action>(chain: List<Interception<S, A>>) : List<Interception<S, A>> by chain {

    constructor(vararg items: Interception<S, A>) : this(items.toList())

    /**
     * Builder class for [Interceptions].
     *
     * Example:
     * ```
     * Interceptions.Builder<SampleState, SampleAction>()
     *     .add(CustomInterception())
     *     .add(targetedInterceptions)
     *     .adapter { state, action -> if (state.that && action is SampleAction.DoSomething) SampleAction.DoThat else action }
     *     .filter { _, action -> action !is SampleAction.IgnoreMe }
     *     .pipe { _, action -> Log.d("Sample", "Chain result: $action") }
     *     .build()
     * ```
     */
    @InterceptionsBuilderMarker
    class Builder<S : State, A : Action> {

        private val chain = mutableListOf<Interception<S, A>>()

        /**
         * Adds the given [interceptions] to the chain.
         */
        fun add(vararg interceptions: Interception<S, A>) = add(interceptions.toList())

        /**
         * Adds the given [interceptions] to the chain.
         */
        fun add(interceptions: List<Interception<S, A>>) = apply { chain.addAll(interceptions) }

        /**
         * Creates an [Adapter] and adds it to the chain.
         */
        fun adapter(debugName: String = "", adapt: (state: S, action: A) -> A) = add(Adapter(debugName, adapt))

        /**
         * Creates a [Filter] and adds it to the chain.
         */
        fun filter(debugName: String = "", predicate: (state: S, action: A) -> Boolean) = add(Filter(debugName, predicate))

        /**
         * Creates a [Pipe] and adds it to the chain.
         */
        fun pipe(debugName: String = "", before: (state: S, action: A) -> Unit = { _, _ -> }, after: (state: S, action: A?) -> Unit = { _, _ -> }) =
            add(Pipe(debugName, before, after))

        /**
         * Creates a [Command] and adds it to the chain.
         */
        fun command(debugName: String = "", react: ReactionScope.(action: A) -> Reaction<S, A>) = add(Command(debugName, react))

        /**
         * Creates a [LiveCommand] and adds it to the chain.
         */
        fun liveCommand(debugName: String = "", react: LiveReactionScope.(action: A) -> LiveReaction<S, A>) = add(LiveCommand(debugName, react))

        /**
         * Builds an [Interceptions] instance with the created chain.
         */
        fun build() = Interceptions(*chain.toTypedArray())

        /**
         * Convenience wrapper around [Interceptions.Builder.Targeted].
         *
         * Example:
         * ```
         * interceptions<SampleState, SampleAction> {
         *     on<SampleAction.DoSomething> {
         *         adapter { state, action -> if (state.that) SampleAction.DoThat else action }
         *         filter { state, _ -> !state.doingSomething }
         *         pipe { _, _ -> Log.d("Sample", "SampleAction.DoSomething dispatched") }
         *     }
         * }
         * ```
         */
        inline fun <reified T : A> on(build: Interceptions.Builder.Targeted<S, A, T>.() -> Unit) = add(
            Interceptions.Builder.Targeted<S, A, T>(target = { it is T }).apply(build).build()
        )

        /**
         * Builder class for a list of [Interception] instances targeted to a specific action.
         *
         * Example:
         * ```
         * Interceptions.Builder.Targeted<SampleState, SampleAction, SampleAction.DoSomething>(target = { it is SampleAction.DoSomething })
         *     .adapter { state, action -> if (state.that) SampleAction.DoThat else action }
         *     .filter { state, _ -> !state.doingSomething }
         *     .pipe { _, _ -> Log.d("Sample", "SampleAction.DoSomething dispatched") }
         *     .build()
         * ```
         *
         * @param[T] Type of the targeted action.
         * @param[target] Lambda expression to determine the target depending on the given `action`. Should return `true` if it matches the target,
         * otherwise `false`.
         */
        @Suppress("UNCHECKED_CAST")
        @InterceptionsBuilderMarker
        class Targeted<S : State, A : Action, T : A>(private val target: (action: A?) -> Boolean) {

            private val chain = mutableListOf<Interception<S, A>>()

            /**
             * Creates an [Adapter] that only calls [adapt] for the targeted action.
             *
             * *Note: `action` in [adapt] is already cast to [T].*
             */
            fun adapter(debugName: String = "", adapt: (state: S, action: T) -> A) = apply {
                chain.add(Adapter(debugName) { state, action -> if (target(action)) adapt(state, action as T) else action })
            }

            /**
             * Creates a [Filter] that only calls [predicate] for the targeted action.
             *
             * *Note: `action` in [predicate] is already cast to [T].*
             */
            fun filter(debugName: String = "", predicate: (state: S, action: T) -> Boolean) = apply {
                chain.add(Filter(debugName) { state, action -> if (target(action)) predicate(state, action as T) else true })
            }

            /**
             * Creates a [Pipe] that only calls [before] and [after] for the targeted action.
             *
             * *Note: `action` in [before] and [after] is already cast to [T].*
             */
            fun pipe(debugName: String = "", before: (state: S, action: T) -> Unit = { _, _ -> }, after: (state: S, action: T) -> Unit = {_, _ ->}) = apply {
                chain.add(
                    Pipe(
                        debugName,
                        { state, action -> if (target(action)) before(state, action as T) },
                        { state, action -> if (target(action)) after(state, action as T) }
                    )
                )
            }

            /**
             * Creates a consuming [Command] that only calls [block] for the targeted action.
             *
             * *Note: [block] is additionally called with targeted `action`.*
             *
             * @see[Reaction.Consuming]
             */
            fun consumingCommand(debugName: String = "", immediateAction: A, block: suspend (state: S, action: T, dispatch: (A) -> Unit) -> Unit) = apply {
                chain.add(
                    Command(debugName) { action ->
                        if (target(action)) consuming(immediateAction) { state, dispatch -> block(state, action as T, dispatch) } else ignoring()
                    }
                )
            }

            /**
             * Creates a forwarding [Command] that only calls [block] for the targeted action.
             *
             * *Note: [block] is additionally called with targeted `action`.*
             *
             * @see[Reaction.Forwarding]
             */
            fun forwardingCommand(debugName: String = "", block: suspend (state: S, action: T, dispatch: (A) -> Unit) -> Unit) = apply {
                chain.add(
                    Command(debugName) { action ->
                        if (target(action)) forwarding { state, dispatch -> block(state, action as T, dispatch) } else ignoring()
                    }
                )
            }

            /**
             * Creates a consuming [LiveCommand] that only calls [block] for the targeted action.
             *
             * *Note: [block] is additionally called with targeted `action`.*
             *
             * @see[LiveReaction.Consuming]
             */
            fun consumingLiveCommand(
                debugName: String = "",
                immediateAction: A,
                block: suspend CoroutineScope.(state: S, action: T) -> ReceiveChannel<A>
            ) = apply {
                chain.add(
                    LiveCommand(debugName) { action ->
                        if (target(action)) consuming(immediateAction) { state -> block(state, action as T) } else ignoring()
                    }
                )
            }

            /**
             * Creates a forwarding [LiveCommand] that only calls [block] for the targeted action.
             *
             * *Note: [block] is additionally called with targeted `action`.*
             *
             * @see[LiveReaction.Forwarding]
             */
            fun forwardingLiveCommand(debugName: String = "", block: suspend CoroutineScope.(state: S, action: T) -> ReceiveChannel<A>) = apply {
                chain.add(
                    LiveCommand(debugName) { action ->
                        if (target(action)) forwarding { state -> block(state, action as T) } else ignoring()
                    }
                )
            }

            /**
             * Builds a list of targeted [Interception] instances.
             */
            fun build(): List<Interception<S, A>> = chain
        }
    }
}

/**
 * Convenience wrapper around [Interceptions.Builder].
 *
 * Example:
 * ```
 * interceptions<SampleState, SampleAction> {
 *     add(CustomInterception())
 *     add(targetedInterceptions)
 *     adapter { state, action -> if (state.that && action is SampleAction.DoSomething) SampleAction.DoThat else action }
 *     filter { _, action -> action !is SampleAction.IgnoreMe }
 *     pipe { _, action -> Log.d("Sample", "Chain result: $action") }
 * }
 * ```
 */
inline fun <S : State, A : Action> interceptions(build: Interceptions.Builder<S, A>.() -> Unit) = Interceptions.Builder<S, A>().apply(build).build()
