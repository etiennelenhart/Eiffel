package com.etiennelenhart.eiffel.state

import android.arch.lifecycle.LiveData

/**
 * A view model exposing a single observable view state.
 *
 * @property[state] The observable view state.
 */
interface StateViewModel<T : ViewState> {

    val state: LiveData<T>
}
