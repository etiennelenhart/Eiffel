package com.etiennelenhart.eiffel.livedata

/**
 * Invokes the given lambda expression for a successful LiveData value.
 *
 * @param[V] Type of the LiveData's value.
 * @param[block] The lambda expression to call.
 */
inline fun <V> Resource<V, *>.onSuccess(block: (value: V) -> Unit): Resource<V, *> {
    if (this is Resource.Success) block(value)
    return this
}

/**
 * Invokes the given lambda expression for a pending LiveData value.
 *
 * @param[V] Type of the LiveData's value.
 * @param[block] The lambda expression to call.
 */
inline fun <V> Resource<V, *>.onPending(block: (value: V) -> Unit): Resource<V, *> {
    if (this is Resource.Pending) block(value)
    return this
}

/**
 * Invokes the given lambda expression for a failed LiveData value.
 *
 * @param[V] Type of the LiveData's value.
 * @param[E] Type of error for [Resource.Failure].
 * @param[block] The lambda expression to call.
 */
inline fun <V, E> Resource<V, E>.onFailure(block: (value: V, error: E) -> Unit): Resource<V, E> {
    if (this is Resource.Failure) block(value, error)
    return this
}
