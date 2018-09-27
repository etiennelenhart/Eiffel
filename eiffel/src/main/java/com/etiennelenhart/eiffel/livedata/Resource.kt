package com.etiennelenhart.eiffel.livedata

import androidx.lifecycle.LiveData

/**
 * Wrapper class to associate a status to a LiveData value.
 *
 * @param[V] Type of the LiveData's value.
 * @param[E] Type of error for [Resource.Failure].
 * @property[value] LiveData's actual value.
 */
sealed class Resource<out V, out E>(val value: V) {

    /**
     * Resource variant signaling a successful LiveData value.
     *
     * @param[value] Actual value.
     */
    class Success<out V>(value: V) : Resource<V, Nothing>(value)

    /**
     * Resource variant signaling a pending LiveData value.
     *
     * @param[value] Actual value.
     */
    class Pending<out V>(value: V) : Resource<V, Nothing>(value)

    /**
     * Resource variant signaling a failed LiveData value.
     *
     * @param[value] Actual value.
     * @param[error] Error that occurred.
     * @property[error] Error that occurred.
     */
    class Failure<out V, out E>(value: V, val error: E) : Resource<V, E>(value)
}

/**
 * Convenience function to create a [Resource.Success] variant.
 *
 * @param[value] LiveData's actual value.
 * @return The [Resource.Success] variant.
 */
fun <T> LiveData<Resource<T, *>>.successValue(value: T) = Resource.Success(value)

/**
 * Convenience function to create a [Resource.Pending] variant.
 *
 * @param[value] LiveData's actual value.
 * @return The [Resource.Pending] variant.
 */
fun <T> LiveData<Resource<T, *>>.pendingValue(value: T) = Resource.Pending(value)

/**
 * Convenience function to create a [Resource.Failure] variant.
 *
 * @param[value] LiveData's actual value.
 * @param[error] Error that occurred.
 * @return The [Resource.Failure] variant.
 */
fun <T, E> LiveData<Resource<T, E>>.failureValue(value: T, error: E) = Resource.Failure(value, error)
