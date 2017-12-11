package com.etiennelenhart.eiffel.state

import android.arch.lifecycle.MutableLiveData

/**
 * Convenience property to access the current view state's value.
 *
 * @throws[KotlinNullPointerException] when the state's value has not been set.
 */
var <T : ViewState> StateViewModel<T>.currentState
    get() = state.value!!
    set(value) {
        (state as MutableLiveData).value = value
    }