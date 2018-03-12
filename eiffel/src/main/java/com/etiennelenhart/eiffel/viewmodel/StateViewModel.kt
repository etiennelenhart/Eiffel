package com.etiennelenhart.eiffel.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.etiennelenhart.eiffel.state.ViewState

/**
 * A [ViewModel] exposing a single observable view state.
 *
 * To ensure an immutable state, always set a new state using the
 * [updateState] function and calling copy(...) on the provided current state.
 *
 * @param[T] Type of view state to expose.
 * @property[state] Observable view state.
 * @property[stateInitialized] The current value of the view state.
 * @property[currentState] Current value of the view state.
 * Throws [KotlinNullPointerException] when the state's initial value has not been set.
 */
abstract class StateViewModel<T : ViewState> : ViewModel() {

    abstract val state: LiveData<T>

    protected val stateInitialized
        get() = state.value != null
    protected val currentState
        get() = state.value!!

    /**
     * Sets the initial view state if not already initialized.
     *
     * @param[viewState] Initial view state.
     */
    protected fun initState(viewState: T) {
        if (!stateInitialized) (state as MutableLiveData).value = viewState
    }

    /**
     * Updates the current state by applying the supplied lambda expression.
     *
     * May be used like this:
     *
     * ```
     * updateState { it.copy(sample = true) }
     * ```
     *
     * @param[update] Lambda expression that receives the current state and should return a new updated state.
     * @throws[KotlinNullPointerException] when the state's initial value has not been set.
     */
    protected inline fun updateState(update: (currentState: T) -> T) {
        (state as MutableLiveData).value = update(currentState)
    }
}
