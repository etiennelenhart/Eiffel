package com.etiennelenhart.eiffel

/**
 * Type of error that occurred while executing a command or before notifying a LiveData value.
 */
@Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
interface ErrorType {

    /**
     * Convenience [ErrorType] if specific type of error is not needed.
     */
    @Deprecated("All Result-related functionality is now deprecated. See github.com/etiennelenhart/Eiffel/blob/master/MIGRATION3-4.md#result-related-functionality for more info.")
    object Unspecified : ErrorType
}
