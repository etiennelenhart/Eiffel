package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status

/**
 * The result of a pending or finished command that returns some data.
 *
 * @property[status] The command's current status.
 * @property[data] Data the command returned.
 */
sealed class ResultWithData<out T>(val status: Status, val data: T) {
    /**
     * Result variant signaling a successful command that returns data.
     *
     * @param[data] Data the command should return.
     */
    class Success<out T>(data: T) : ResultWithData<T>(Status.SUCCESS, data)

    /**
     * Result variant signaling a pending command that returns data.
     *
     * @param[data] Data the command should return.
     */
    class Pending<out T>(data: T) : ResultWithData<T>(Status.PENDING, data)

    /**
     * Result variant signaling a failed command that returns data.
     *
     * @param[default] Default value of the data that should have been returned.
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error<out T>(default: T, val type: ErrorType = ErrorType.Unspecified) : ResultWithData<T>(Status.ERROR, default)
}
