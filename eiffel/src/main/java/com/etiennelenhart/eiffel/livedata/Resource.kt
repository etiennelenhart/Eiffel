package com.etiennelenhart.eiffel.livedata

import android.arch.lifecycle.LiveData
import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status

/**
 * Wrapper class to associate a status to a LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @property[status] Current status of the LiveData value.
 * @property[value] LiveData's actual value.
 */
sealed class Resource<out T>(val status: Status, val value: T) {
    /**
     * Resource variant signaling a successful LiveData value.
     *
     * @param[value] Actual value.
     */
    class Success<out T>(value: T) : Resource<T>(Status.SUCCESS, value)

    /**
     * Resource variant signaling a pending LiveData value.
     *
     * @param[value] Actual value.
     */
    class Pending<out T>(value: T) : Resource<T>(Status.PENDING, value)

    /**
     * Resource variant signaling a failed LiveData value.
     *
     * @param[value] Actual value.
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error<out T>(value: T, val type: ErrorType = ErrorType.Unspecified) : Resource<T>(Status.ERROR, value)
}

/**
 * Convenience function to create a [Resource.Success] variant.
 *
 * @param[value] LiveData's actual value.
 * @return The [Resource.Success] variant.
 */
fun <T> LiveData<Resource<T>>.successValue(value: T) = Resource.Success(value)

/**
 * Convenience function to create a [Resource.Pending] variant.
 *
 * @param[value] LiveData's actual value.
 * @return The [Resource.Pending] variant.
 */
fun <T> LiveData<Resource<T>>.pendingValue(value: T) = Resource.Pending(value)

/**
 * Convenience function to create a [Resource.Error] variant.
 *
 * @param[value] LiveData's actual value.
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Resource.Error] variant.
 */
fun <T> LiveData<Resource<T>>.errorValue(value: T, type: ErrorType = ErrorType.Unspecified) = Resource.Error(value, type)
