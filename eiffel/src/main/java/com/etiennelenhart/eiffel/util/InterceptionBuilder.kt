package com.etiennelenhart.eiffel.util

import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.interception.Adapter
import com.etiennelenhart.eiffel.interception.Filter
import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Pipe
import com.etiennelenhart.eiffel.interception.adapter
import com.etiennelenhart.eiffel.interception.command.Command
import com.etiennelenhart.eiffel.interception.command.LiveCommand
import com.etiennelenhart.eiffel.interception.command.LiveReaction
import com.etiennelenhart.eiffel.interception.command.LiveReactionScope
import com.etiennelenhart.eiffel.interception.command.Reaction
import com.etiennelenhart.eiffel.interception.command.ReactionScope
import com.etiennelenhart.eiffel.interception.command.command
import com.etiennelenhart.eiffel.interception.command.consuming
import com.etiennelenhart.eiffel.interception.command.forwarding
import com.etiennelenhart.eiffel.interception.command.ignoring
import com.etiennelenhart.eiffel.interception.command.liveCommand
import com.etiennelenhart.eiffel.interception.filter
import com.etiennelenhart.eiffel.interception.pipe
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * Convenience builder class to create a list of [Interception] using kotlin DSL.
 *
 * @see [InterceptionBuilder.build] and [buildInterceptions]
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
open class InterceptionBuilder<S : State, A : Action> {

    /**
     * Mutable list of added [Interception].
     *
     * Once the building is done call [toList] to retrieve an immutable version of [interceptions].
     */
    private val interceptions = mutableListOf<Interception<S, A>>()

    /**
     * Add an interception to the list of [interceptions].
     *
     * Also useful if you have created your own [Interception] class, and want to still use the [InterceptionBuilder].
     *
     * Example:
     *
     * class MyInterception <S: State, A: Action> : Interception<S, A>() {
     *    ...
     *    ...
     * }
     *
     * buildInterceptions<SampleState, SampleAction> {
     *     add {
     *        object: MyInterception() {
     *           ...
     *        }
     *     }
     * }
     *
     * @param[block] Lambda expression that returns the [Interception].
     */
    fun add(block: () -> Interception<S, A>) {
        interceptions.add(block())
    }

    /**
     * Add a [Filter] to the [interceptions]. Passes [predicate] to the [filter] convenience function.
     *
     * Example:
     *
     * ```
     * addFilter { state, action ->
     *    state.value != state.otherValue
     * }
     * ```
     *
     * @see filter
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[predicate] Lambda expression to be passed to [filter].
     */
    fun addFilter(debugName: String = "", predicate: (state: S, action: A) -> Boolean) = add {
        filter(debugName, predicate)
    }

    /**
     * Add a [Filter] that targets a specific [A] [Action].
     *
     * Similar to [addFilter] except this will only run if the 'action' is the same type as [T].
     *
     * Example:
     *
     * ```
     * addFilterOn<SampleAction.Loading> { state, action ->
     *    state.value != state.otherValue
     * }
     * ```
     *
     * @see addFilter
     * @param[T] Target [Action] to filter on.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[predicate] Lambda expression to be passed to [filter].
     */
    inline fun <reified T : A> addFilterOn(
        debugName: String = "",
        crossinline predicate: (state: S, action: A) -> Boolean
    ) = addFilter(debugName) { state, action ->
        if (action is T) predicate(state, action) else true
    }

    /**
     * Add a [Pipe] to the [interceptions]. Passes [before] and [after] to the [pipe] helper function.
     *
     * Example:
     *
     * ```
     * addPipe(
     *     before = { _, action -> Timber.d("Before: $action") }
     *     after = { _, action -> Timber.d("After: $action") }
     * )
     * ```
     *
     * @see pipe
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[before] Lambda expression called with the current [State] and received [Action].
     * @param[after] Lambda expression called with the current [State] and updated [Action] from next item.
     */
    fun addPipe(
        debugName: String = "",
        before: (state: S, action: A) -> Unit,
        after: (state: S, action: A?) -> Unit
    ) = add { pipe(debugName, before, after) }

    /**
     * Similar to [addPipe] except it will only execute on a specific [Action] of type [T].
     *
     * * Example:
     *
     * ```
     * addPipeOn<SampleAction.Error>(
     *     before = { _, action -> Timber.d(action.error, "Before: $action") }
     *     after = { _, action -> Timber.d(action.error, "After: $action") }
     * )
     * ```
     *
     * @see addPipe
     * @param[T] Target [Action] to pipe on.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[before] Lambda expression called with the current [State] and received [Action].
     * @param[after] Lambda expression called with the current [State] and updated [Action] from next item.
     */
    inline fun <reified T : A> addPipeOn(
        debugName: String = "",
        crossinline before: (state: S, action: A) -> Unit,
        crossinline after: (state: S, action: A?) -> Unit
    ) = addPipe(
        debugName = debugName,
        before = { state, action -> if (action is T) before(state, action) },
        after = { state, action -> if (action is T) after(state, action) }
    )

    /**
     * Add a [Pipe] to the [interceptions]. Passes [before] to the [pipe] helper function. To pipe
     * on both the 'before' and 'after', see [addPipe].
     *
     * Example:
     *
     * ```
     * addBeforePipe { state, action ->
     *    Timber.d("Received action: $action")
     * }
     * ```
     *
     * @see pipe
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[before] Lambda expression called with the current [State] and received [Action].
     */
    fun addBeforePipe(
        debugName: String = "",
        before: (state: S, action: A) -> Unit
    ) = add { pipe(debugName, before) }

    /**
     * A 'before' only version of [addPipeOn].
     *
     * @param[T] Target [Action] to pipe on.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[before] Lambda expression called with the current [State] and received [Action].
     */
    inline fun <reified T : A> addBeforePipeOn(
        debugName: String = "",
        crossinline before: (state: S, action: T) -> Unit
    ) = addPipeOn<T>(
        debugName = debugName,
        before = { state, action -> before(state, action as T) },
        after = { _, _ -> }
    )

    /**
     * Add a [Pipe] to the [interceptions]. Passes [after] to the [pipe] helper function. To pipe
     * on both the 'before' and 'after', see [addPipe].
     *
     * Example:
     *
     * ```
     * addBeforePipe { state, action ->
     *    Timber.d("Received action: $action")
     * }
     * ```
     *
     * @see pipe
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[after] Lambda expression called with the current [State] and received [Action].
     */
    fun addAfterPipe(
        debugName: String = "",
        after: (state: S, action: A) -> Unit
    ) = add { pipe(debugName, after) }

    /**
     * A 'after' only version of [addPipeOn].
     *
     * @param[T] Target [Action] to pipe on.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[after] Lambda expression called with the current [State] and received [Action].
     */
    inline fun <reified T : A> addAfterPipeOn(
        debugName: String = "",
        crossinline after: (state: S, action: T) -> Unit
    ) = addPipeOn<T>(
        debugName = debugName,
        before = { _, _ -> },
        after = { state, action -> after(state, action as T) }
    )

    /**
     * Add an [Adapter] to [interceptions], passes [adapt] lambda to [adapter].
     *
     * Example:
     *
     * ```
     * addAdapter { action ->
     *    if (action is SampleAction.Refresh) SampleAction.LoadData
     *    else action
     * }
     * ```
     *
     * @see Adapter
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[adapt] Lambda expression that adapts the given [Action] to a new one.
     */
    fun addAdapter(debugName: String = "", adapt: (action: A) -> A) = add {
        adapter(debugName, adapt)
    }

    /**
     * Similar to [addAdapter] except it will only execute on a specific [Action] of type [T].
     *
     * Example:
     *
     * ```
     * addAdapterOn<SampleAction.Error> { action ->
     *     // do work
     * }
     * ```
     *
     * @see addAdapter
     * @param[T] Target [Action] to adapt on.
     * @param[R] Adapted [Action].
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[adapt] Lambda expression that adapts the given [Action] to a new one.
     */
    inline fun <reified T : A, R : A> addAdapterOn(
        debugName: String = "",
        crossinline adapt: (action: T) -> R
    ) = addAdapter(debugName) { action -> if (action is T) adapt(action) else action }

    /**
     * Add a [Command] to [interceptions], passes [react] lambda to [command].
     *
     * @see Command
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[react] Lambda expression called with the received [Action]. Use one of [ReactionScope.consuming], [ReactionScope.forwarding]
     * or [ReactionScope.ignoring]. (see [Command.react])
     */
    fun addCommand(
        debugName: String = "",
        react: ReactionScope.(action: A) -> Reaction<S, A>
    ) = add { command(debugName, react) }

    /**
     * Similar to [addCommand] except it targets a single [Action] of type [T].
     *
     * Useful if you want to launch some async request on a specific action.  If you supply a [immediateAction],
     * then the created [Command] will always return [Reaction.Consuming] when 'action' is [T].  If
     * [immediateAction] is left null, the created [Command] will instead return [Reaction.Forwarding].
     *
     * If the 'action' doesn't match the target [T], then the created [Command] will return [Reaction.Ignoring].
     *
     * Example:
     *
     * ```
     * // Uses Reaction.Consuming.  'LoadData' is dispatched immediately.
     * addCommandOn<SampleAction.LoadData>(SampleAction.Loading) { _, action, dispatch ->
     *   val data = api.loadData(action.url)
     *   dispatch(if (data.size > 0) SampleAction.HasData else SampleAction.NoData)
     * }
     *
     * // Create a side effect without dispatching an immediate action.
     * addCommandOn<SampleAction.DeleteRecord> { _, action, _ ->
     *     myDatabase.delete(action.user.id)
     * }
     * ```
     *
     * @param[T] Target [Action] to create the [Command] with.
     * @param[immediateAction] If null a [Reaction.Forwarding] will be used, otherwise [Reaction.Consuming] will be used.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression that is called asynchronously when the 'action' is [T].
     * To update state when done call provided `dispatch` lambda.
     */
    inline fun <reified T : A> addCommandOn(
        immediateAction: A? = null,
        debugName: String = "",
        crossinline block: suspend (state: S, action: T, dispatch: (A) -> Unit) -> Unit
    ) = addCommand(debugName) { action ->
        if (action !is T) ignoring()
        else {
            val command: suspend (state: S, dispatch: (A) -> Unit) -> Unit =
                { state, dispatch -> block(state, action, dispatch) }

            if (immediateAction == null) forwarding(command)
            else consuming(immediateAction, command)
        }
    }

    /**
     * Convenience function for creating a [Reaction.Consuming] [Command] targeting a specific [Action].
     *
     * Example:
     *
     * ```
     * addConsumingCommandOn<SampleAction.FetchData>(SampleAction.Loading) { _, action, dispatch ->
     *   val data = api.loadData(action.url)
     *   dispatch(if (data.size > 0) SampleAction.HasData else SampleAction.NoData)
     * }
     * ```
     *
     * @see addCommandOn
     * @param[T] Target [Action] to create the [Command] with.
     * @param[immediateAction] [Action] to immediately dispatch.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression that is called asynchronously when the 'action' is [T].
     * To update state when done call provided `dispatch` lambda.
     */
    inline fun <reified T : A> addConsumingCommandOn(
        immediateAction: A,
        debugName: String = "",
        crossinline block: suspend (state: S, action: T, dispatch: (A) -> Unit) -> Unit
    ) = addCommandOn(immediateAction, debugName, block)

    /**
     * Convenience function for creating a [Reaction.Consuming] [Command] targeting a specific [Action].
     *
     * Example:
     *
     * ```
     * addForwardingCommandOn<SampleAction.DeleteRecord> { _, action, _ ->
     *     myDatabase.delete(action.user.id)
     * }
     * ```
     *
     * @see addCommandOn
     * @param[T] Target [Action] to create the [Command] with.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression that is called asynchronously when the 'action' is [T].
     * To update state when done call provided `dispatch` lambda.
     */
    inline fun <reified T : A> addForwardingCommandOn(
        debugName: String = "",
        crossinline block: suspend (state: S, action: T, dispatch: (A) -> Unit) -> Unit
    ) = addCommandOn(null, debugName, block)

    /**
     * Add a [LiveCommand] to [interceptions], passes [react] lambda to [liveCommand].
     *
     * @see LiveCommand
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[react] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    fun addLiveCommand(
        debugName: String = "",
        react: LiveReactionScope.(action: A) -> LiveReaction<S, A>
    ) = add { liveCommand(debugName, react) }

    /**
     * Similar to [addLiveCommand] except it targets a single [Action] of type [T].
     *
     * A [LiveCommand] implementation of [addCommandOn]
     *
     * @param[T] Target [Action] to create the [Command] with.
     * @param[immediateAction] If null a [Reaction.Forwarding] will be used, otherwise [Reaction.Consuming] will be used.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    inline fun <reified T : A> addLiveCommandOn(
        immediateAction: A? = null,
        debugName: String = "",
        noinline block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>
    ) = addLiveCommand(debugName) { action ->
        when {
            action !is T -> ignoring()
            immediateAction == null -> forwarding(block)
            else -> consuming(immediateAction, block)
        }
    }

    /**
     * [LiveCommand] implementation of [addConsumingCommandOn].
     *
     * @see addLiveCommandOn
     * @param[T] Target [Action] to create the [Command] with.
     * @param[immediateAction] [Action] to immediately dispatch.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    inline fun <reified T : A> addConsumingLiveCommand(
        immediateAction: A,
        debugName: String = "",
        noinline block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>
    ) = addLiveCommandOn<T>(immediateAction, debugName, block)

    /**
     * [LiveCommand] implementation of [addForwardingCommandOn].
     *
     * @see addLiveCommandOn
     * @param[T] Target [Action] to create the [Command] with.
     * @param[debugName] Name to use when logging is enabled with [Eiffel.debugMode].
     * @param[block] Suspending lambda expression returning a [Channel] to receive from. To update state call [Channel.send] on this channel.
     */
    inline fun <reified T : A> addForwardingLiveCommand(
        debugName: String = "",
        noinline block: suspend CoroutineScope.(state: S) -> ReceiveChannel<A>
    ) = addLiveCommandOn<T>(null, debugName, block)

    /**
     * Public accessor to get an immutable [List] of the created list of [Interception].
     */
    fun toList() = interceptions.toList()

    companion object {

        /**
         * Convenience builder function to create a list of [Interception] using a kotlin
         * DSL syntax.
         *
         * @sample
         * ```
         * val interceptors: List<Interceptor<SampleState, SampleAction> = InterceptionBuilder.build {
         *    addFilter { state, _ ->
         *       state.valueOne < state.valueTwo
         *    }
         *
         *    addPipe { state, action ->
         *        Timber.tag("SampleViewModel").d("Action: $action -> State: $state")
         *    }
         *
         *    addPipeAfter { _, action ->
         *       Timber.tag("SampleViewModel").d("Next action: $action")
         *    }
         *
         *    // data class ErrorLoading(val exception: Throwable): SampleAction
         *    addPipeOn<SampleAction.ErrorLoading> { state ->
         *        Timber.tag("SampleViewModel").e(this.exception, "Failed to fetch data, state: $state")
         *    }
         * }
         * ```
         *
         * @param[S] Type of [State] to receive.
         * @param[A] Type of supported [Action].
         * @param[block] DSL lambda called with the scope of an [InterceptionBuilder].
         * @return A list of [Interception].
         */
        fun <S : State, A : Action> build(
            block: InterceptionBuilder<S, A>.() -> Unit
        ) = InterceptionBuilder<S, A>().apply(block).toList()
    }
}

/**
 * Convenience function for calling [InterceptionBuilder.build].
 *
 * @sample
 * ```
 * val interceptors: List<Interceptors<SampleState, SampleAction> = buildInterceptions {
 *     addFilter { state, action ->
 *         state.someValue != state.otherValue
 *     }
 * }
 * ```
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[block] DSL lambda called with the scope of an [InterceptionBuilder].
 * @return A list of [Interception].
 */
fun <S : State, A : Action> buildInterceptions(
    block: InterceptionBuilder<S, A>.() -> Unit
): List<Interception<S, A>> = InterceptionBuilder.build(block)
