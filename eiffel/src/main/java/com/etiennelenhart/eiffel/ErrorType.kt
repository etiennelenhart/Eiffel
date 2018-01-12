package com.etiennelenhart.eiffel

/**
 * Type of error that occurred while executing a command or before notifying a LiveData value.
 */
interface ErrorType {

    /**
     * Convenience [ErrorType] if specific type of error is not needed.
     */
    object Unspecified : ErrorType
}
