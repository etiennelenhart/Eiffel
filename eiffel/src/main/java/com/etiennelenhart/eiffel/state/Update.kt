package com.etiennelenhart.eiffel.state

/**
 * When invoked, should update the provided state according to the given action.
 *
 * Example:
 *
 * ```
 * class SampleStateUpdate : Update<SampleState, SampleAction> {
 *
 *     override fun invoke(state: SampleState, action: SampleAction) = when (action) {
 *         SampleAction.DoingSample -> state.copy(doing = "something")
 *         else -> state
 *     }
 * }
 * ```
 *
 * @param[S] Type of [State] that can be updated.
 * @param[A] Type of supported [Action].
 */
interface Update<S : State, A : Action> {

    /**
     * Update the provided [state] according to the given [action].
     *
     * @param[state] Current [State].
     * @param[action] Dispatched [Action].
     * @return The updated [State].
     */
    operator fun invoke(state: S, action: A): S
}

/**
 * Convenience builder function that returns an object implementing [Update]. Passes provided lambda to overridden operator.
 *
 * @param[block] Lambda expression called with the current [State] and dispatched [Action]. Return the updated [State].
 * @return An object implementing [Update].
 */
fun <S : State, A : Action> update(block: (state: S, action: A) -> S): Update<S, A> {
    return object : Update<S, A> {
        override fun invoke(state: S, action: A) = block(state, action)
    }
}
