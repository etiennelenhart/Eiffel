package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression depending on the command's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[onSuccess] The lambda expression to call for a succeeded command.
 * @param[onError] The lambda expression to call for a failed command.
 */
inline fun <T, R> Result<T>.on(onSuccess: (data: T) -> R, onError: (type: ErrorType) -> R) = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(type)
}

/**
 * Invokes the given lambda expression when the command succeeded.
 *
 * @param[T] Type of the result's data.
 * @param[block] The lambda expression to call.
 */
inline fun <T> Result<T>.isSuccess(block: (data: T) -> Unit) = on(block, {})

/**
 * Invokes the given lambda expression when the command failed.
 *
 * @param[T] Type of the result's data.
 * @param[block] The lambda expression to call.
 */
inline fun <T> Result<T>.isError(block: (type: ErrorType) -> Unit) = on({}, block)
