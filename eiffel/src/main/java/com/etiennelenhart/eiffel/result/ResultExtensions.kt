package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression for this result variant and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expressions' return type.
 * @param[succeeded] Expression to call for a successful command's result.
 * @param[isPending] Expression to call for a pending command's result.
 * @param[failed] Expression to call for a failed command's result.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.on(succeeded: (data: T) -> R, isPending: (data: T) -> R, failed: (type: ErrorType) -> R) = when (this) {
    is Result.Success -> succeeded(data)
    is Result.Pending -> isPending(data)
    is Result.Error -> failed(type)
}

/**
 * Invokes the given lambda expression when the command was successful and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.succeeded(block: (data: T) -> R) {
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
inline fun <T, R> Result<T>.isPending(block: (data: T) -> R) {
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
inline fun <T, R> Result<T>.failed(block: (type: ErrorType) -> R) {
    if (this is Result.Error) block(type)
}
