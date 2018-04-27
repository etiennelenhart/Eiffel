package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.result.live.LiveResult
import com.etiennelenhart.eiffel.result.live.failure
import com.etiennelenhart.eiffel.result.live.success

/**
 * Result of a finished command.
 *
 * @param[T] Type of the returned data.
 */
sealed class Result<out T> {

    /**
     * Result variant signaling a successful command.
     *
     * @param[T] Type of result data.
     * @param[data] Data the command should return.
     * @property[data] Data the command returned.
     */
    class Success<out T>(val data: T) : Result<T>()

    /**
     * Result variant signaling a failed command.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error(val type: ErrorType = ErrorType.Unspecified) : Result<Nothing>()
}

/**
 * Convenience function to create a [Result.Success] variant.
 *
 * @param[T] Type of result data.
 * @param[data] Data the command should return.
 * @return The [Result.Success] variant.
 */
fun <T> succeeded(data: T) = Result.Success(data)

/**
 * Convenience function to create a [Result.Success] without data.
 *
 * @return The [Result.Success] variant.
 */
fun succeeded() = succeeded(Unit)

/**
 * Convenience function to create a [Result.Error] variant.
 *
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Result.Error] variant.
 */
fun failed(type: ErrorType = ErrorType.Unspecified) = Result.Error(type)

/**
 * Converts this [Result] to an instance of [LiveResult].
 *
 * @param[T] Type of result data.
 * @return The [LiveResult] converted from this [Result].
 */
fun <T> Result<T>.toLive() = when (this) {
    is Result.Success -> success(data)
    is Result.Error -> failure(type)
}
