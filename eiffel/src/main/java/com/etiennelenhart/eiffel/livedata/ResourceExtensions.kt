package com.etiennelenhart.eiffel.livedata

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the given lambda expression for a successful LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Resource<T>.isSuccess(block: (value: T) -> R) {
    if (this is Resource.Success) block(value)
}

/**
 * Invokes the given lambda expression for a pending LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Resource<T>.isPending(block: (value: T) -> R) {
    if (this is Resource.Pending) block(value)
}

/**
 * Invokes the given lambda expression for a failed LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to call.
 */
inline fun <T, R> Resource<T>.isError(block: (value: T, type: ErrorType) -> R) {
    if (this is Resource.Error) block(value, type)
}
