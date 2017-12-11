package com.etiennelenhart.eiffel.state

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * A [ViewModel] exposing a single observable view state.
 *
 * To ensure an immutable state, always set a new state using the
 * [updateState] function and calling copy(...).
 * @property[state] The observable view state.
 */
abstract class StateViewModel<T : ViewState> : ViewModel() {

    abstract val state: LiveData<T>

    /**
     * Sets the initial view state.
     *
     * @param[viewState] Initial view state.
     */
    protected fun initState(viewState: T) {
        (state as MutableLiveData).value = viewState
    }

    /**
     * Updates the current state by applying the 'update' lambda.
     *
     * May be used like this:
     *
     * ```
     * updateState { it.copy(sample = true) }
     * ```
     *
     * @param[update] Lambda to update the current view state.
     * @throws[KotlinNullPointerException] when the state's initial value has not been set.
     */
    protected inline fun updateState(update: (currentState: T) -> T) {
        (state as MutableLiveData).value = update(state.value!!)
    }
}
