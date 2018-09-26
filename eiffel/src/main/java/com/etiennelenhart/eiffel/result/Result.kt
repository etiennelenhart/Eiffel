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
@Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
sealed class Result<out T> {

    /**
     * Result variant signaling a successful command.
     *
     * @param[T] Type of result data.
     * @param[data] Data the command should return.
     * @property[data] Data the command returned.
     */
    @Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
    class Success<out T>(val data: T) : Result<T>()

    /**
     * Result variant signaling a failed command.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    @Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
    class Error(val type: ErrorType = ErrorType.Unspecified) : Result<Nothing>()
}

/**
 * Convenience function to create a [Result.Success] variant.
 *
 * @param[T] Type of result data.
 * @param[data] Data the command should return.
 * @return The [Result.Success] variant.
 */
@Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
fun <T> succeeded(data: T) = Result.Success(data)

/**
 * Convenience function to create a [Result.Success] without data.
 *
 * @return The [Result.Success] variant.
 */
@Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
fun succeeded() = succeeded(Unit)

/**
 * Convenience function to create a [Result.Error] variant.
 *
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [Result.Error] variant.
 */
@Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
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
