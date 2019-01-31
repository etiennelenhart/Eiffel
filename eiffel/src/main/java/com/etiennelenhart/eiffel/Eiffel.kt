package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.logger.Logger

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
