package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the given lambda expression when the command was successful.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Result<T>.isSuccess(block: (data: T) -> R) {
    if (this is Result.Success) block(data)
}

/**
 * Invokes the given lambda expression when the command is pending.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Result<T>.isPending(block: (data: T) -> R) {
    if (this is Result.Pending) block(data)
}

/**
 * Invokes the given lambda expression when the command failed.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Result<T>.isError(block: (data: T, type: ErrorType) -> R) {
    if (this is Result.Error) block(data, type)
}
