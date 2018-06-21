package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.ViewState

/**
 * State that adapts a [ViewState] to a bindable state for data binding.
 *
 * Since [ViewState]s should expose layout agnostic properties, any adaptions
 * needed to properly display the current state can be made inside this state's
 * [refresh] method.
 *
 * @param[T] Type of view state to adapt.
 */
interface BindingState<in T : ViewState> {

    /**
     * Make any adaptions needed to properly display the current state here.
     * For example, when using BaseObservable:
     *
     * ```
     * class SampleBindingState : BaseObservable(), BindingState<SampleViewState> {
     *     @get:Bindable
     *     var sampleResId by notifyBinding(R.string.none, BR.sampleResId)
     *         private set
     *
     *     override fun refresh(state: SampleViewState) {
     *         sampleResId = if (state.sampling) R.string.sample else R.string.none
     *     }
     * }
     * ```
     *
     * @param[state] View state to adapt.
     */
    fun refresh(state: T)
}
