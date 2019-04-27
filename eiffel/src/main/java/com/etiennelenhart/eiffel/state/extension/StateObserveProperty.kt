package com.etiennelenhart.eiffel.state.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.util.distinctUntilChanged
import com.etiennelenhart.eiffel.util.map

/**
 * Used to observe a specific property of this state from a [LifecycleOwner] like [FragmentActivity] or [Fragment].
 *
 * @param[S] Type of this [State].
 * @param[P] Type of the observed property.
 * @param[owner] [LifecycleOwner] that controls observation.
 * @param[propertyValue] Lambda expression returning the value of the property that should be observed.
 * @param[onChanged] Lambda expression that is called with an updated property value.
 */
fun <S : State, P> LiveData<S>.observeProperty(owner: LifecycleOwner, propertyValue: (state: S) -> P, onChanged: (value: P) -> Unit) =
    map(propertyValue).distinctUntilChanged().observe(owner, Observer(onChanged))

internal fun <S : State, P> LiveData<S>.observePropertyForever(propertyValue: (state: S) -> P, onChanged: (value: P) -> Unit) =
    map(propertyValue).distinctUntilChanged().observeForever(onChanged)
