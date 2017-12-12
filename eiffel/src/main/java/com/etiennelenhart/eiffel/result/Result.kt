package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status

/**
 * Result of a pending or finished command.
 *
 * @property[status] Command's current status.
 */
sealed class Result(val status: Status) {
    /**
     * Result variant signaling a successful command.
     */
    object Success : Result(Status.SUCCESS)

    /**
     * Result variant signaling a pending command.
     */
    object Pending : Result(Status.PENDING)

    /**
     * Result variant signaling a failed command.
     *
     * @param[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     * @property[type] Optional [ErrorType]. Defaults to [ErrorType.Unspecified].
     */
    class Error(val type: ErrorType = ErrorType.Unspecified) : Result(Status.ERROR)
}
