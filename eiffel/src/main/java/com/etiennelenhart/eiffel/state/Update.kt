package com.etiennelenhart.eiffel.state

/**
 * Function used to update the state according to the given action.
 *
 * Example:
 *
 * ```
 * fun updateSampleState(state: SampleState, action: SampleAction) = when (action) {
 *     DoSample -> state.copy(doing = "something")
 *     is UpdateSample -> state.copy(sample = action.sample)
 * }
 * ```
 */
typealias Update<State, Action> = (State, Action) -> State
