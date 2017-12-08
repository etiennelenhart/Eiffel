package com.etiennelenhart.eiffel

/**
 * The type of error that occurred while executing a command or notifying a LiveData value.
 */
interface ErrorType {

    /**
     * Convenience [ErrorType] if specific type of error is not needed.
     */
    object Unspecified : ErrorType
}
