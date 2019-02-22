package com.etiennelenhart.eiffel.logger

import android.util.Log
import com.etiennelenhart.eiffel.logger.Logger.Default

/**
 * May be used to create a custom logging solution to override the [Default] behaviour.
 *
 * As an example you could create a [Logger] object that uses Timber or some other
 * custom logging library.
 *
 * Example:
 * ```
 * object: Logger {
 *   override fun log(priority: Int, tag: String, message: String) {
 *       Timber.tag(tag).log(priority, message)
 *   }
 * }
 * ```
 * @see logger For a shorthand way of generating a [Logger]
 */
interface Logger {

    /**
     * Pass the log details off to the [Logger] implementation.
     *
     * Useful if you need to override the details of the log message, or use a different library
     * for handling the logging.
     */
    fun log(priority: Int, tag: String, message: String)

    /**
     * Default implementation of [Logger] which uses [Log.println] to log the messages.
     */
    object Default : Logger {

        override fun log(priority: Int, tag: String, message: String) {
            Log.println(priority, tag, message)
        }
    }
}

/**
 * Convenience function to build an instance of [Logger].
 *
 * Example:
 * ```
 * // Log all messages using Timber with a 'info' priority.
 * logger { _, tag, message ->
 *    Timber.tag(tag).i(message)
 * }
 * ```
 *
 * @param[log] Lambda for logging.
 * @return Object implementing [Logger].
 */
inline fun logger(crossinline log: (priority: Int, tag: String, message: String) -> Unit): Logger {
    return object : Logger {
        override fun log(priority: Int, tag: String, message: String) {
            log(priority, tag, message)
        }
    }
}
