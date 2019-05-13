package com.etiennelenhart.eiffel.util

import androidx.lifecycle.*

class NonNullMediatorLiveData<T : Any> : MediatorLiveData<T>() {

    override fun getValue() = super.getValue()!!

    fun observe(owner: LifecycleOwner, onChanged: (T) -> Unit) = observe(owner, Observer<T> { it?.let(onChanged) })
}

internal fun <T : Any> LiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    return NonNullMediatorLiveData<T>().apply {
        addSource(this@nonNull) { if (it != null) value = it }
    }
}

internal fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@distinctUntilChanged) { if (it != value) value = it }
    }
}

internal fun <T, R> LiveData<T>.map(mapFunction: (value: T) -> R) = Transformations.map(this, mapFunction)
