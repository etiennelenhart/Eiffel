package com.etiennelenhart.eiffel.result.live

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression depending on the result's type and returns the expression's result.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[R] The expressions' return type.
 * @param[onPending] The lambda expression to call for a pending result.
 * @param[onSuccess] The lambda expression to call for a success result.
 * @param[onFailure] The lambda expression to call for a failure result.
 * @return The respective expression's result.
 */
inline fun <P, S, R> LiveResult<P, S>.fold(onPending: (data: P) -> R, onSuccess: (data: S) -> R, onFailure: (type: ErrorType) -> R) = when (this) {
    is LiveResult.Pending -> onPending(data)
    is LiveResult.Success -> onSuccess(data)
    is LiveResult.Failure -> onFailure(type)
}

/**
 * Invokes the given lambda expression when the result is pending and forwards this result.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[block] The lambda expression to call.
 * @return this result.
 */
inline fun <P, S> LiveResult<P, S>.isPending(block: (data: P) -> Unit) = also { it.fold(block, {}, {}) }

/**
 * Invokes the given lambda expression when the result is success and forwards this result.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[block] The lambda expression to call.
 * @return this result.
 */
inline fun <P, S> LiveResult<P, S>.isSuccess(block: (data: S) -> Unit) = also { it.fold({}, block, {}) }

/**
 * Invokes the given lambda expression when the result is failure and forwards this result.
 *
 * @param[P] Type of the result's intermediate data.
 * @param[S] Type of the result's success data.
 * @param[block] The lambda expression to call.
 * @return this result.
 */
inline fun <P, S> LiveResult<P, S>.isFailure(block: (type: ErrorType) -> Unit) = also { it.fold({}, {}, block) }
