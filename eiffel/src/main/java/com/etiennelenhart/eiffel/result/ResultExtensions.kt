package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression depending on the command's result and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expressions' return type.
 * @param[onSuccess] The lambda expression to call for a succeeded command.
 * @param[onError] The lambda expression to call for a failed command.
 * @return The respective expression's result.
 */
inline fun <T, R> Result<T>.fold(onSuccess: (data: T) -> R, onError: (type: ErrorType) -> R) = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(type)
}

/**
 * Invokes the given lambda expression when the command succeeded and forwards this result.
 *
 * @param[T] Type of the result's data.
 * @param[block] The lambda expression to call.
 * @return this result.
 */
inline fun <T> Result<T>.isSuccess(block: (data: T) -> Unit) = also { it.fold(block, {}) }

/**
 * Invokes the given lambda expression when the command failed and forwards this result.
 *
 * @param[T] Type of the result's data.
 * @param[block] The lambda expression to call.
 * @return this result.
 */
inline fun <T> Result<T>.isError(block: (type: ErrorType) -> Unit) = also { it.fold({}, block) }
