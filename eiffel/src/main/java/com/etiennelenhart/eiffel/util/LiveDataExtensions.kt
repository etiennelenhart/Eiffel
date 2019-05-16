package com.etiennelenhart.eiffel.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class NonNullMediatorLiveData<T : Any> : MediatorLiveData<T>() {

    override fun getValue() = super.getValue()!!

    fun observe(owner: LifecycleOwner, onChanged: (T) -> Unit) = observe(owner, Observer<T> { it?.let(onChanged) })
}

internal fun <T : Any> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    return NonNullMediatorLiveData<T>().apply {
        addSource(this@nonNull) { if (it != null) value = it }
    }
}
