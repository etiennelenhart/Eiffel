package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.plugin.Dispatcher
import com.etiennelenhart.eiffel.plugin.EiffelPlugin
import com.etiennelenhart.eiffel.plugin.event.Event
import com.etiennelenhart.eiffel.plugin.logger.DefaultLoggerPlugin
import com.etiennelenhart.eiffel.plugin.logger.LoggerPlugin
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * The config for the Debug feature of Eiffel.
 */
internal interface DebugConfig {

    /**
     * Global flag for enabling/disabling debug mode.
     *
     * Can be overridden on a per-ViewModel basis.
     * @see EiffelViewModel.debug
     */
    val enabled: Boolean

    /**
     * Plugins that will react to [Event]'s dispatched by a [EiffelViewModel].
     *
     * Currently there is one supplied plugin for logging to the logcat.
     * @see LoggerPlugin
     */
    val plugins: List<EiffelPlugin>

    /**
     * Custom implementation for dispatching an [Event].
     * @see Dispatcher
     */
    val dispatcher: Dispatcher

    /**
     * The default implementation for [DebugConfig].
     *
     * Call [Eiffel.debugMode] to enable debug mode and set a new config.
     */
    object Default : DebugConfig {

        /**
         * Default the debug mode to disabled.
         */
        override val enabled = false

        /**
         * Default to use the [DefaultLoggerPlugin] for logging [Event]'s to the logcat.
         */
        override val plugins = listOf<EiffelPlugin>(DefaultLoggerPlugin())

        /**
         * Use the default [Dispatcher].
         */
        override val dispatcher = Dispatcher.Default
    }
}