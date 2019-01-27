package com.etiennelenhart.eiffel.binding.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.etiennelenhart.eiffel.binding.BindingState
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.extension.observe

/**
 * Used to observe an this state from a [LifecycleOwner] like [FragmentActivity] or [Fragment] and
 * simultaneously refresh a [BindingState].
 *
 * @param[S] Type of this [State].
 * @param[owner] [LifecycleOwner] that controls observation.
 * @param[bindingState] [BindingState] to refresh with a newly emitted state.
 * @param[onChanged] Optional lambda expression that is called with a newly emitted state.
 */
fun <S : State> LiveData<S>.observe(owner: LifecycleOwner, bindingState: BindingState<S>, onChanged: (newState: S) -> Unit = {}) = observe(owner) {
    bindingState.refresh(it)
    onChanged(it)
}
