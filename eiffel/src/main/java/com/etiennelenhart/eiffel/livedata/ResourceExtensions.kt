package com.etiennelenhart.eiffel.livedata

import com.etiennelenhart.eiffel.ErrorType

/**
 * Invokes the respective lambda expression depending on the resource's type and returns its result.
 *
 * @param[T] Type of the LiveData's data.
 * @param[R] The expressions' return type.
 * @param[onPending] The lambda expression to call for a pending LiveData value.
 * @param[onSuccess] The lambda expression to call for a successful LiveData value.
 * @param[onFailure] The lambda expression to call for a failed LiveData value.
 * @return The respective expression's result.
 */
inline fun <T, R> Resource<T>.fold(onPending: (value: T) -> R, onSuccess: (value: T) -> R, onFailure: (value: T, type: ErrorType) -> R) = when (this) {
    is Resource.Pending -> onPending(value)
    is Resource.Success -> onSuccess(value)
    is Resource.Failure -> onFailure(value, type)
}

/**
 * Invokes the given lambda expression for a pending LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[block] The lambda expression to call.
 */
inline fun <T> Resource<T>.isPending(block: (value: T) -> Unit) = fold(block, {}, { _, _ -> })

/**
 * Invokes the given lambda expression for a successful LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[block] The lambda expression to call.
 */
inline fun <T> Resource<T>.isSuccess(block: (value: T) -> Unit) = fold({}, block, { _, _ -> })

/**
 * Invokes the given lambda expression for a failed LiveData value.
 *
 * @param[T] Type of the LiveData's value.
 * @param[block] The lambda expression to call.
 */
inline fun <T> Resource<T>.isFailure(block: (value: T, type: ErrorType) -> Unit) = fold({}, {}, block)
