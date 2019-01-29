package com.etiennelenhart.eiffel

import android.app.Application
import android.util.Log
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Debug logging utility for logging all actions and state updates.
 *
 * Logging can be enabled globally by calling [EiffelLogger.enable] or on a per [EiffelViewModel]
 * basis by overriding [EiffelViewModel.isDebugMode].
 *
 * Allows the ability to supply custom logging class.
 *
 * @sample
 * ```
 * // Create a logging class
 * class TimberLogger : Logger {
 *    override fun log(tag: String, msg: String) {
 *        Timber.tag(tag).d(msg)
 *    }
 * }
 *
 * // Supply it to the constructor
 * EiffelLogger(TimberLogger())
 * ```
 *
 * @param[logger] Logging interface, defaults to [Log].
 */
class EiffelLogger(
    private var logger: Logger? = null
) {

    init {
        if (logger == null) logger = defaultLogger
    }

    /**
     * Log a message without a custom tag.
     *
     * @param[msg] Message to log.
     */
    internal fun log(msg: String) {
        logger?.log(tagPrefix, msg)
    }

    /**
     * Log a message with a custom tag.
     *
     * @sample
     * ```
     * EiffelLogger().log("SomeViewModel", "Message")
     *
     * // Logs:
     * // D/Eiffel:SomeViewModel: Message
     *
     * @param[tag] Custom tag to append to the [tagPrefix].
     * @param[msg] Message to log.
     */
    internal fun log(tag: String, msg: String) {
        logger?.log("$tagPrefix:$tag", msg)
    }

    companion object {

        /**
         * Prefix to add to the log tag.
         */
        private const val tagPrefix: String = "Eiffel"

        /**
         * Default logging interface, defaults to [DefaultLogger] which logs with [Log].
         */
        private var defaultLogger: Logger? = null

        /**
         * Check if [EiffelLogger.enable] has been called globally.
         */
        val isEnabled: Boolean
            get() = defaultLogger != null

        /**
         * Static function to enable the logger globally.
         *
         * Useful if you do not want to manually enable the logger in each [EiffelViewModel].  It
         * can be enabled anywhere, but a good place is in your [Application] entry point.
         *
         * @sample
         * ```
         * class App : Application() {
         *
         *    override onCreate() {
         *       super.onCreate()
         *
         *       Timber.plant(DebugTree())
         *
         *       EiffelLogger.enable(object: Logger {
         *          override log(tag: String, message: String) {
         *             Timber.tag(tag).d(message)
         *          }
         *       })
         *    }
         * }
         * ```
         *
         * @param[logger] Logger interface, defaults to [Log].
         */
        fun enable(logger: Logger = DefaultLogger()) {
            defaultLogger = logger
        }
    }

    /**
     * Abstraction for providing a custom logging solution.
     */
    interface Logger {

        fun log(tag: String, msg: String)
    }

    /**
     * A default logger implementation that uses [Log] to log the messages
     */
    private class DefaultLogger : Logger {

        override fun log(tag: String, msg: String) {
            Log.d(tag, msg)
        }
    }
}