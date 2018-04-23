package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType

/**
 * Error type for a failed attempted result.
 *
 * @param[exception] The exception to forward from the attempt.
 * @property[exception] The exception thrown during the attempt.
 */
class AttemptError(val exception: Exception) : ErrorType

/**
 * Wraps the given lambda expression with a try catch block and maps it to a [Result].
 *
 * @param[R] The expression's return type.
 * @param[block] The lambda expression to attempt.
 * @return A [Result] with the expression's result or with an [AttemptError].
 */
inline fun <R> attempt(block: () -> R) = try {
    succeeded(block())
} catch (e: Exception) {
    failed(AttemptError(e))
}
