package com.etiennelenhart.eiffel.state.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.etiennelenhart.eiffel.state.State

/**
 * Used to observe this state from a [LifecycleOwner] like [FragmentActivity] or [Fragment].
 *
 * @param[S] Type of this [State].
 * @param[owner] [LifecycleOwner] that controls observation.
 * @param[onChanged] Lambda expression that is called with an updated state.
 */
fun <S : State> LiveData<S>.observe(owner: LifecycleOwner, onChanged: (state: S) -> Unit) = observe(owner, Observer(onChanged))
