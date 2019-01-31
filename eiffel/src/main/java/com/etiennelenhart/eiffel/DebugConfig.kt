package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.logger.Logger

internal interface DebugConfig {

    val enabled: Boolean
    val logger: Logger

    object Default : DebugConfig {

        override val enabled = false
        override val logger: Logger = Logger.Default
    }
}
