package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the given lambda expression when the command was successful and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.onSuccess(block: (data: T) -> R) {
    if (this is Result.Success) block(data)
}

/**
 * Invokes the given lambda expression when the command is pending and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.onPending(block: (data: T) -> R) {
    if (this is Result.Pending) block(data)
}

/**
 * Invokes the given lambda expression when the command failed and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.onError(block: (data: T, type: ErrorType) -> R) {
    if (this is Result.Error) block(data, type)
}
