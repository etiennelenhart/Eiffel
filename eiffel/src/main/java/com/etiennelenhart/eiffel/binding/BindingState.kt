package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State

/**
 * State that adapts a [State] to a bindable state for data binding.
 *
 * Since [State]s should expose layout agnostic properties, any adaptions
 * needed to properly display the current state can be made inside this binding state's
 * [refresh] method.
 *
 * @param[T] Type of state to adapt.
 */
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel and BindableState.")
interface BindingState<in T : State> {

    /**
     * Make any adaptions needed to properly display the current state here.
     * For example, when using BaseObservable:
     *
     * ```
     * class SampleBindingState : BaseObservable(), BindingState<SampleState> {
     *     @get:Bindable
     *     var sampleResId by notifyBinding(R.string.none, BR.sampleResId)
     *         private set
     *
     *     override fun refresh(state: SampleState) {
     *         sampleResId = if (state.sampling) R.string.sample else R.string.none
     *     }
     * }
     * ```
     *
     * @param[state] State to adapt.
     */
    fun refresh(state: T)
}
