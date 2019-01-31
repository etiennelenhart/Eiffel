package com.etiennelenhart.eiffel.logger

import android.util.Log

interface Logger {

    fun log(priority: Int, tag: String, message: String)

    object Default : Logger {

        override fun log(priority: Int, tag: String, message: String) {
            Log.println(priority, tag, message)
        }
    }
}

/**
 * Convenience function to build an instance of [Logger].
 *
 * @param[logger] Lambda for logging.
@return Object implementing [Logger].
 */
inline fun log(crossinline logger: (priority: Int, tag: String, message: String) -> Unit): Logger {
    return object : Logger {
        override fun log(priority: Int, tag: String, message: String) {
            logger(priority, tag, message)
        }
    }
}
