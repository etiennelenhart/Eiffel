package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.logger.Logger
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

object Eiffel {

    internal var debugConfig: DebugConfig = DebugConfig.Default
        private set

    /**
     * Enable or disable Eiffel's debug mode for all [EiffelViewModel].
     *
     * To disable [debugMode] for a specific [EiffelViewModel] override [EiffelViewModel.excludeFromDebug].
     *
     * You can override the [Logger.Default] by passing in your own implementation of [Logger].
     * Example:
     * ```
     * Eiffel.debugMode(true, logger { priority, tag, message ->
     *     Timber.tag(tag).log(priority, message)
     * })
     * ```
     *
     * @param[enabled] Globally enable the debug mode for the whole app.
     * @param[logger] [Logger] implementation to use.
     */
    fun debugMode(enabled: Boolean, logger: Logger = Logger.Default) {
        debugConfig = object : DebugConfig {
            override val enabled = enabled
            override val logger = logger
        }
    }
}
