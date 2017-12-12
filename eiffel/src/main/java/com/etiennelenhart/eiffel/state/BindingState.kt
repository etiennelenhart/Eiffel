package com.etiennelenhart.eiffel.state

/**
 * State that adapts a [ViewState] to a bindable state for data binding.
 *
 * Since [ViewState]s should expose view agnostic properties, any adaptions
 * needed to properly display the current state can be made inside this state's
 * [refresh] method.
 *
 * @param[T] Type of view state to adapt.
 */
interface BindingState<in T : ViewState> {

    /**
     * Perform any adaptions needed to properly display the current state here.
     * For example:
     *
     * ```
     * class SampleBindingState(val loading: ObservableBoolean = ObservableBoolean(false)) : BindingState<SampleViewState> {
     *     fun refresh(state: SampleViewState) {
     *         loading.set(state.status == Status.PENDING)
     *     }
     * }
     * ```
     *
     * @param[state] View state to adapt.
     */
    fun refresh(state: T)
}
