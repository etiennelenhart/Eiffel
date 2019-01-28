package com.etiennelenhart.eiffel.util

import com.etiennelenhart.eiffel.interception.Adapter
import com.etiennelenhart.eiffel.interception.Filter
import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Pipe
import com.etiennelenhart.eiffel.interception.adapter
import com.etiennelenhart.eiffel.interception.command.Command
import com.etiennelenhart.eiffel.interception.command.LiveCommand
import com.etiennelenhart.eiffel.interception.command.LiveReaction
import com.etiennelenhart.eiffel.interception.command.Reaction
import com.etiennelenhart.eiffel.interception.command.command
import com.etiennelenhart.eiffel.interception.command.liveCommand
import com.etiennelenhart.eiffel.interception.filter
import com.etiennelenhart.eiffel.interception.pipe
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State

/**
 * Convenience builder class to create a list of [Interception] using kotlin DSL.
 *
 * @see [InterceptorBuilder.build] and [buildInterceptors]
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
class InterceptorBuilder<S : State, A : Action> {

    /**
     * Mutable list of added [Interception].
     *
     * Once the building is done call [toList] to retrieve an immutable version of [interceptions].
     */
    private val interceptions = mutableListOf<Interception<S, A>>()

    /**
     * Add a [Filter] to the [interceptions]. Passes [predicate] to the [filter] convenience function.
     *
     * @sample
     * ```
     * addFilter { state, action ->
     *    state.value != state.otherValue
     * }
     * ```
     *
     * @see filter
     * @param[predicate] Lambda expression to be passed to [filter].
     * @return Instance of [InterceptorBuilder].
     */
    fun addFilter(predicate: suspend (state: S, action: A) -> Boolean) = this.apply {
        interceptions.add(filter(predicate))
    }

    /**
     * Add a [Pipe] to the [interceptions]. Passes [before] and [after] to the [pipe] convenience function.
     *
     * If you only want to do work on the [before] you can just supply the lambda outside the
     * brackets. If you need both or just the [after] you will need to use named parameters
     * or [addPipeBefore] and [addPipeAfter].
     *
     * @sample
     * ```
     * // Only before
     * addPipe { state, action ->
     *    Timber.d("Received action: $action")
     * }
     *
     * // Both before and after
     * addPipe(
     *     before = { _, action -> Timber.d("Action: $action") }
     *     after = { _, action -> Timber.d("Next Action: $action") }
     * )
     * ```
     *
     * @see pipe
     * @param[before] Lambda expression called with the current [State] and received [Action].
     * @param[after] Lambda expression called with the current [State] and updated [Action] from next item.
     * @return Instance of [InterceptorBuilder].
     */
    fun addPipe(
        before: suspend (state: S, action: A) -> Unit = { _, _ -> },
        after: suspend (state: S, action: A) -> Unit = { _, _ -> }
    ) = this.apply { interceptions.add(pipe(before, after)) }

    /**
     * Add a [Pipe] targeting a specific [Action] to [interceptions].
     *
     * Similar to [addPipe] except you can target a specific action [T] of [A].  The passed in
     * lambdas are scoped to the [Action], so `this == [A]`.
     *
     * @sample
     * ```
     * // Given: data class ErrorLoading(val exception: Throwable): SampleAction
     * addPipeOn<SampleAction.ErrorLoading> { state ->
     *     // 'this' Scoped to SampleAction.ErrorLoading
     *    Timber.e(this.exception, "Failed to fetch data, state: $state")
     * }
     * ```
     *
     * @see addPipe
     * @param[T] Specific [Action] you want to target.
     * @param[before] Lambda expression called with the current [State] and scoped to received [Action].
     * @param[after] Lambda expression called with the current [State] and scoped to the updated [Action] from next item.
     * @return Instance of [InterceptorBuilder].
     */
    inline fun <reified T : A> addPipeOn(
        crossinline before: suspend A.(state: S) -> Unit = {},
        crossinline after: suspend A.(state: S) -> Unit = {}
    ) = addPipe(
        before = { state, action -> if (action is T) before(action, state) },
        after = { state, action -> if (action is T) after(action, state) }
    )

    /**
     * Add a before [Pipe].
     *
     * Similar to [addPipe] except only targeting the [before].
     *
     * @sample
     * ```
     * addPipeBefore { state ->
     *    Timber.e(this.exception, "Failed to fetch data, state: $state")
     * }
     * ```
     *
     * @see addPipe
     * @param[before] Lambda expression called with the current [State] and received [Action].
     * @return Instance of [InterceptorBuilder].
     */
    fun addPipeBefore(before: suspend (state: S, action: A) -> Unit) = addPipe(before = before)

    /**
     * Add an after [Pipe].
     *
     * Similar to [addPipe] except only targeting the [after].
     *
     * @sample
     * ```
     * addPipeAfter { state -> // Do something }
     * ```
     *
     * @see addPipe
     * @param[after] Lambda expression called with the current [State] and updated [Action] from next item.
     * @return Instance of [InterceptorBuilder].
     */
    fun addPipeAfter(after: suspend (state: S, action: A) -> Unit) = addPipe(after = after)

    /**
     * Add an [Adapter] to [interceptions], passes [adapt] lambda to [adapter].
     *
     * @sample
     * ```
     * addAdapter { action ->
     *    if (action is SampleAction.Refresh) SampleAction.LoadData
     *    else action
     * }
     * ```
     *
     * @see Adapter
     * @param[adapt] Lambda expression that adapts the given [Action] to a new one.
     * @return Instance of [InterceptorBuilder].
     */
    fun addAdapter(adapt: (action: A) -> A) = this.apply {
        interceptions.add(adapter(adapt))
    }

    /**
     * Add a [Command] to [interceptions], passes [react] lambda to [command].
     *
     * @see Command
     * @param[react] Lambda expression called with the received [Action]. Return either [Reaction.Consuming] or [Reaction.Ignoring].
     * @return Instance of [InterceptorBuilder].
     */
    fun addCommand(react: (action: A) -> Reaction<S, A>) = this.apply {
        interceptions.add(command(react))
    }

    /**
     * Add a [LiveCommand] to [interceptions], passes [react] lambda to [liveCommand].
     *
     * @see LiveCommand
     * @param[react] Lambda expression called with the received [Action]. Return either [LiveReaction.Consuming] or [LiveReaction.Ignoring].
     * @return Instance of [InterceptorBuilder].
     */
    fun addLiveCommand(react: (action: A) -> LiveReaction<S, A>) = this.apply {
        interceptions.add(liveCommand(react))
    }

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
         * val interceptors: List<Interceptor<SampleState, SampleAction> = InterceptorBuilder.build {
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
         * @param[block] DSL lambda called with the scope of an [InterceptorBuilder].
         * @return A list of [Interception].
         */
        fun <S : State, A : Action> build(
            block: InterceptorBuilder<S, A>.() -> Unit
        ) = InterceptorBuilder<S, A>().apply(block).toList()
    }
}

/**
 * Convenience function for calling [InterceptorBuilder.build].
 *
 * @sample
 * ```
 * val interceptors: List<Interceptors<SampleState, SampleAction> = buildInterceptors {
 *     addFilter { state, action ->
 *         state.someValue != state.otherValue
 *     }
 * }
 * ```
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 * @param[block] DSL lambda called with the scope of an [InterceptorBuilder].
 * @return A list of [Interception].
 */
fun <S : State, A : Action> buildInterceptors(
    block: InterceptorBuilder<S, A>.() -> Unit
): List<Interception<S, A>> = InterceptorBuilder.build(block)