package com.etiennelenhart.eiffel.state

import android.arch.lifecycle.LiveData

/**
 * A view model exposing a single observable view state.
 *
 * To ensure an immutable state, set a new state like this:
 *
 * `currentState = currentState.copy(...)`
 *
 * @property[state] The observable view state.
 */
interface StateViewModel<T : ViewState> {

    val state: LiveData<T>
}
