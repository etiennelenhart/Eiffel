package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.plugin.Dispatcher
import com.etiennelenhart.eiffel.plugin.EiffelPlugin
import com.etiennelenhart.eiffel.plugin.event.Event
import com.etiennelenhart.eiffel.plugin.logger.DefaultLoggerPlugin
import com.etiennelenhart.eiffel.plugin.logger.LoggerPlugin

/**
 * Static Eiffel object containing all config and debug logic
 */
object Eiffel {

    /**
     * Debug configuration.
     *
     * Defaults to [DebugConfig.Default], but can be overridden by calling [debugMode].
     */
    internal var debugConfig: DebugConfig = DebugConfig.Default
        private set

    /**
     * Enable or disable Eiffel's debug mode.
     *
     * @sample
     * Basic example:
     * ```
     * class MyDebugApp : Application {
     *
     *   override onCreate() {
     *      super.onCreate()
     *
     *      // By default a LoggerPlugin is added
     *      Eiffel.debugMode(true)
     *   }
     * }
     *
     * class MyReleaseApp : Application {
     *
     *   override onCreate() {
     *      super.onCreate()
     *
     *      // Never log anything in production
     *      Eiffel.debugMode(true, listOf(ReleaseLoggerPlugin()))
     *   }
     * }
     * ```
     *
     * @sample
     * Complex example
     * ```
     * Eiffel.debugMode(
     *    enabled = true,
     *    plugins = listOf(
     *       DefaultLoggerPlugin(
     *         logger = log { priority, tag, message ->
     *             Timber.tag(tag).log(priority, message)
     *         },
     *         transformer = transform { event -> "Event: ${event.toString()}" }
     *       )
     *    )
     * )
     * ```
     *
     * @param[enabled] Globally enable the debug mode for the whole app.
     * @param[plugins] Eiffel plugins to use.
     * @param[dispatcher] Implementation for dispatching the [Event].
     */
    fun debugMode(
        enabled: Boolean,
        plugins: List<EiffelPlugin> = listOf(),
        dispatcher: Dispatcher = Dispatcher.Default
    ) {
        debugConfig = object : DebugConfig {
            override val enabled: Boolean = enabled
            override val plugins: List<EiffelPlugin> = createPluginList(plugins)
            override val dispatcher: Dispatcher = dispatcher
        }
    }

    /**
     * Create a list of plugins, ensuring that a [LoggerPlugin] always exists.
     *
     * @param[plugins] List of plugins to modify.
     * @return List of [EiffelPlugin] with a guaranteed [LoggerPlugin].
     */
    private fun createPluginList(plugins: List<EiffelPlugin>) = plugins
        .toMutableList()
        .let { list ->
            val hasLogger = list.find { it is LoggerPlugin<*> } != null
            if (!hasLogger) list.add(0, DefaultLoggerPlugin())

            return@let list
        }
        .toList()
}

