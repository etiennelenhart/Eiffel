package com.etiennelenhart.eiffel.result.live

import com.etiennelenhart.eiffel.ErrorType

/**
 * Maps this result to a new one with the given lambda expression applied to the result's intermediate data.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[P2] Type of the mapped result's intermediate data.
 * @param[transform] Expression to transform the result's error type.
 */
inline fun <P, S, P2> LiveResult<P, S>.mapPending(transform: (data: P) -> P2) = when (this) {
    is LiveResult.Pending -> LiveResult.Pending(transform(data))
    is LiveResult.Success -> this
    is LiveResult.Failure -> this
}

/**
 * Maps this result to a new one with the given lambda expression applied to the result's success data.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[S2] Type of the mapped result's success data.
 * @param[transform] Expression to transform the result's data.
 */
inline fun <P, S, S2> LiveResult<P, S>.map(transform: (data: S) -> S2) = when (this) {
    is LiveResult.Pending -> this
    is LiveResult.Success -> LiveResult.Success(transform(data))
    is LiveResult.Failure -> this
}

/**
 * Maps this result to a new one with the given lambda expression applied to the result's error type.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[transform] Expression to transform the result's error type.
 */
inline fun <P, S> LiveResult<P, S>.mapFailure(transform: (type: ErrorType) -> ErrorType) = when (this) {
    is LiveResult.Pending -> this
    is LiveResult.Success -> this
    is LiveResult.Failure -> LiveResult.Failure(transform(type))
}
