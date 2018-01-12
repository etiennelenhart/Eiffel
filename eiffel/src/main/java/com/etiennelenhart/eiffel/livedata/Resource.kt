package com.etiennelenhart.eiffel.livedata

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
