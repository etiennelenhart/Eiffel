package com.etiennelenhart.eiffel.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

internal fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@distinctUntilChanged) { if (it != value) value = it }
    }
}

internal fun <T, R> LiveData<T>.map(mapFunction: (value: T) -> R) = Transformations.map(this, mapFunction)
