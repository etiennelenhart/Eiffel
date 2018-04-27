package com.etiennelenhart.eiffel.result.live

import com.etiennelenhart.eiffel.ErrorType

/**
 * Current result of a command with intermediate values.
 *
 * @param[P] Type of intermediate data.
 * @param[S] Type of success data.
 */
sealed class LiveResult<out P, out S> {

    /**
     * LiveResult variant signaling a pending status.
     *
     * @param[data] Intermediate data the command should yield.
     * @property[data] Intermediate data the command yielded.
     */
    class Pending<out P>(val data: P) : LiveResult<P, Nothing>()

    /**
     * LiveResult variant signaling a success status.
     *
     * @param[data] Data the command should return.
     * @property[data] Data the command returned.
     */
    class Success<out S>(val data: S) : LiveResult<Nothing, S>()

    /**
     * LiveResult variant signaling a failure status.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Failure(val type: ErrorType = ErrorType.Unspecified) : LiveResult<Nothing, Nothing>()
}

/**
 * Convenience function to create a [LiveResult.Pending] variant.
 *
 * @param[P] Type of intermediate data.
 * @param[data] Data the command should return.
 * @return The [LiveResult.Pending] variant.
 */
fun <P> pending(data: P) = LiveResult.Pending(data)

/**
 * Convenience function to create a [LiveResult.Pending] without data.
 *
 * @return The [LiveResult.Pending] variant.
 */
fun pending() = pending(Unit)

/**
 * Convenience function to create a [LiveResult.Success] variant.
 *
 * @param[S] Type of success data.
 * @param[data] Data the command should return.
 * @return The [LiveResult.Success] variant.
 */
fun <S> success(data: S) = LiveResult.Success(data)

/**
 * Convenience function to create a [LiveResult.Success] without data.
 *
 * @return The [LiveResult.Success] variant.
 */
fun success() = success(Unit)

/**
 * Convenience function to create a [LiveResult.Failure] variant.
 *
 * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
 * @return The [LiveResult.Failure] variant.
 */
fun failure(type: ErrorType = ErrorType.Unspecified) = LiveResult.Failure(type)
