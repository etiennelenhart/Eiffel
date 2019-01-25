package com.etiennelenhart.eiffel.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

internal fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    return MediatorLiveData<T>().apply {
        addSource(this@distinctUntilChanged) { if (it != value) value = it }
    }
}
