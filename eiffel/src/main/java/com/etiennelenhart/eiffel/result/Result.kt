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
     * Result variant signaling a successful command that returns data.
     *
     * @param[data] Data the command should return.
     */
    class Success<out T>(data: T) : Result<T>(Status.SUCCESS, data)

    /**
     * Result variant signaling a pending command that returns data.
     *
     * @param[data] Data the command should return.
     */
    class Pending<out T>(data: T) : Result<T>(Status.PENDING, data)

    /**
     * Result variant signaling a failed command that returns data.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error(val type: ErrorType = ErrorType.Unspecified) : Result<Unit>(Status.ERROR, Unit)
}

/**
 * Convenience function to create a [Result.Success] variant.
 *
 * @param[data] Data the command should return.
 * @return The [Result.Success] variant.
 */
fun <T> success(data: T) = Result.Success(data)

/**
 * Convenience function to create a [Result.Success] without any data.
 *
 * @return The [Result.Success] variant.
 */
fun success() = success(Unit)

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
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Result.Error] variant.
 */
fun error(type: ErrorType = ErrorType.Unspecified) = Result.Error(type)
