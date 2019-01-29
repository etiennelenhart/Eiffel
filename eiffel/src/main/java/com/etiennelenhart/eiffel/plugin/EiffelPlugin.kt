package com.etiennelenhart.eiffel.plugin

import com.etiennelenhart.eiffel.plugin.event.Event
import com.etiennelenhart.eiffel.plugin.logger.LoggerPlugin

/**
 * Used to create a custom plugin for Eiffel.
 *
 * The current design only allows the plugin to view the event.  It currently has no power to
 * manipulate or block the chain of plugins.  This may need to be explored in the future.
 *
 * @see LoggerPlugin
 */
interface EiffelPlugin {

    /**
     * Each plugin needs a name so it can be identified.
     *
     * Note: Not currently used, but may be in the future.
     */
    val name: String

    /**
     * All events will flow through each plugin.
     *
     * @param[dispatcher] A string tag of the class that dispatched the [Event].
     * @param[event] The event that has been dispatched.
     */
    fun <E : Event> onEvent(dispatcher: String, event: E)
}