package com.etiennelenhart.eiffel.logger

/**
 * A no-op logger for use in production mode.
 *
 * An opt-in measure to ensure no debug log statements accidentally leak into production.
 */
object ReleaseLogger : Logger {

    override fun log(priority: Int, tag: String, message: String) {}
}