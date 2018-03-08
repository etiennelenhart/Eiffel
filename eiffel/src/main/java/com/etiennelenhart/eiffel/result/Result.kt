package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status

/**
 * Result of a pending or finished command.
 *
 * @param[T] Type of the returned data.
 * @property[status] Command's current status.
 * @property[data] Data the command returned.
 */
sealed class Result<out T>(val status: Status, val data: T) {
    /**
     * Result variant signaling a successful command.
     *
     * @param[data] Data the command should return.
     */
    class Success<out T>(data: T) : Result<T>(Status.SUCCESS, data)

    /**
     * Result variant signaling a pending command.
     *
     * @param[data] Data the command should return.
     */
    class Pending<out T>(data: T) : Result<T>(Status.PENDING, data)

    /**
     * Result variant signaling a failed command.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error<out T>(data: T, val type: ErrorType = ErrorType.Unspecified) : Result<T>(Status.ERROR, data)
}

/**
 * Convenience function to create a [Result.Success] variant.
 *
 * @param[data] Data the command should return.
 * @return The [Result.Success] variant.
 */
fun <T> succeeded(data: T) = Result.Success(data)

/**
 * Convenience function to create a [Result.Success] without any data.
 *
 * @return The [Result.Success] variant.
 */
fun succeeded() = succeeded(Unit)

/**
 * Convenience function to create a [Result.Pending] variant.
 *
 * @param[data] Data the command should return.
 * @return The [Result.Pending] variant.
 */
fun <T> pending(data: T) = Result.Pending(data)

/**
 * Convenience function to create a [Result.Pending] without any data.
 *
 * @return The [Result.Pending] variant.
 */
fun pending() = pending(Unit)

/**
 * Convenience function to create a [Result.Error] variant.
 *
 * @param[data] Data the command should return.
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Result.Error] variant.
 */
fun <T> failed(data: T, type: ErrorType = ErrorType.Unspecified) = Result.Error(data, type)

/**
 * Convenience function to create a [Result.Error] without any data.
 *
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Result.Error] variant.
 */
fun failed(type: ErrorType = ErrorType.Unspecified) = failed(Unit, type)
