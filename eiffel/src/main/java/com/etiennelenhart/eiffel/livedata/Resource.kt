package com.etiennelenhart.eiffel.livedata

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status

/**
 * Wrapper class to associate a [status] to a LiveData value.
 *
 * Actual value should be contained in [data] property.
 */
sealed class Resource<out T>(val status: Status, val data: T) {
    /**
     * Resource variant signaling a successful LiveData operation.
     *
     * Actual value can be contained in [data].
     */
    class Success<out T>(data: T) : Resource<T>(Status.SUCCESS, data)

    /**
     * Resource variant signaling a pending LiveData operation.
     *
     * Actual value can be contained in [data].
     */
    class Pending<out T>(data: T) : Resource<T>(Status.PENDING, data)

    /**
     * Resource variant signaling a failed LiveData operation.
     *
     * Actual value can be contained in [data].
     * [type] property contains an optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error<out T>(data: T, val type: ErrorType = ErrorType.Unspecified) : Resource<T>(Status.ERROR, data)
}
