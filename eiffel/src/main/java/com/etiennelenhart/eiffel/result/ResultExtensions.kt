package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression for this result variant and returns the expression's result.
 *
 * @param[T] Type of the result's data.
 * @param[R] The expressions' return type.
 * @param[success] Expression to call for a successful command's result.
 * @param[isPending] Expression to call for a pending command's result.
 * @param[error] Expression to call for a failed command's result.
 * @return The called expression's result.
 */
inline fun <T, R> Result<T>.on(success: (data: T) -> R, isPending: (data: T) -> R, error: (data: T, type: ErrorType) -> R) = when (this) {
    is Result.Success -> success(data)
    is Result.Pending -> isPending(data)
    is Result.Error -> error(data, type)
}

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
