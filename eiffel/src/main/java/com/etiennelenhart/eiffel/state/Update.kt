package com.etiennelenhart.eiffel.state

/**
 * When invoked, should update the provided state according to the given action.
 *
 * Example:
 *
 * ```
 * class SampleUpdate : Update<SampleState, SampleAction>() {
 *
 *    override fun SampleState.perform(action: SampleAction): SampleState = when (action) {
 *        SampleAction.Loading -> copy(isLoading = true)
 *        SampleAction.Loaded -> copy(isLoading = false, data = action.data)
 *        else -> null
 *    }
 * }
 * ```
 *
 * @param[S] Type of [State] that can be updated.
 * @param[A] Type of supported [Action].
 */
abstract class Update<S : State, A : Action> {

    /**
     * Implementation for updating the state based on [action] scoped to the current state [S].
     *
     * @receiver[S] Current [State].
     * @param[action] Dispatched [Action].
     * @return The updated [State] or `null` if no update has been made.
     */
    abstract fun S.perform(action: A): S?

    /**
     * Updates the provided [state] using [perform] according to the given [action].
     *
     * @param[state] Current [State].
     * @param[action] Dispatched [Action].
     * @return The updated [State] or `null` if no update has been made.
     */
    operator fun invoke(state: S, action: A): S? = state.perform(action)
}

/**
 * Convenience builder function that returns an object implementing [Update]. Passes provided lambda to overridden operator.
 *
 * Example:
 *
 * ```
 * update<SampleState, SampleAction> { action ->
 *   when (action) {
 *      is SampleAction.Loading -> copy(isLoading = true)
 *      is SampleAction.Loaded -> copy(isLoading = false, data = action.data)
 *      else -> null
 *   }
 * }
 * ```
 *
 * @param[perform] Lambda expression called scoped to the current [State] and dispatched [Action]. Return the updated [State]
 * or `null` if no update has been made.
 * @return An object implementing [Update].
 */
fun <S : State, A : Action> update(perform: S.(action: A) -> S?): Update<S, A> {
    return object : Update<S, A>() {
        override fun S.perform(action: A): S? = perform(action)
    }
}
