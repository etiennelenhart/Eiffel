package com.etiennelenhart.eiffel.plugin.logger

import android.util.Log
import com.etiennelenhart.eiffel.plugin.logger.Logger.Default

/**
 * Can be used by [LoggerPlugin] to override the default behaviour of [Default].
 *
 * For example you could create a [Logger] object that uses Timber or some other
 * custom logging library.
 *
 * @see DefaultLoggerPlugin
 */
interface Logger {

    fun log(priority: Int, tag: String, message: String)

    /**
     * A default [Logger] that will just pass the values to [Log.println]
     */
    object Default : Logger {
        override fun log(priority: Int, tag: String, message: String) {
            Log.println(priority, tag, message)
        }
    }

    /**
     * A No-op version of [Logger] for release mode
     */
    object ReleaseLogger : Logger {
        override fun log(priority: Int, tag: String, message: String) {}
    }
}

/**
 * Convenience function for building a [Logger].
 *
 * @param[logger] Lambda expression to log the event.
 */
inline fun log(crossinline logger: (priority: Int, tag: String, message: String) -> Unit): Logger {
    return object : Logger {
        override fun log(priority: Int, tag: String, message: String) {
            logger(priority, tag, message)
        }
    }
}