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
 *         DoSample -> state.copy(doing = "something")
 *         is UpdateSample -> state.copy(sample = action.sample)
 *     }
 * }
 * ```
 */
interface Update<S : State, A : Action> {

    operator fun invoke(state: S, action: A): S
}
