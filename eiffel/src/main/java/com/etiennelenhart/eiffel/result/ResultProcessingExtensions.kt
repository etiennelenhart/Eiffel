package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Runs the given command and returns its result only if this is a successful command's result. Forwards failed results
 * and throws if previous command finished in a pending state.
 *
 * @param[command] Command to run on success.
 * @return Either the given command's result or the current error result.
 * @throws[IllegalStateException] if previous command finished in a pending state.
 */
inline fun <T, T2> Result<T>.then(command: (data: T) -> Result<T2>) = when (this) {
    is Result.Success -> command(data)
    is Result.Pending -> throw IllegalStateException("Unable to proceed with next command, previous one finished in a pending state.")
    is Result.Error -> this
}

/**
 * Maps this result to a new one with the given lambda expression applied to the result's data.
 *
 * @param[T] Type of the result's data.
 * @param[T2] Type of the mapped result's data.
 * @param[transform] Expression to transform the result's data.
 */
inline fun <T, T2> Result<T>.map(transform: (data: T) -> T2) = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Pending -> Result.Pending(transform(data))
    is Result.Error -> Result.Error(transform(data), type)
}

/**
 * Maps this result to a new one with the given lambda expression applied to the result's error type.
 *
 * @param[T] Type of the result's data.
 * @param[transform] Expression to transform the result's error type.
 */
inline fun <T> Result<T>.mapError(transform: (type: ErrorType) -> ErrorType) = when (this) {
    is Result.Error -> Result.Error(data, transform(type))
    else -> this
}
