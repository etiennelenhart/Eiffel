package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.logger.Logger
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Eiffel {

    /**
     * Key for any arguments of an Activity or Fragment which should be passed to a matching secondary constructor of the corresponding view model's [State].
     */
    const val KEY_ARGS = "key_eiffel_arguments"

    internal var actionDispatcher: CoroutineDispatcher = Dispatchers.Default
        private set
    internal var interceptionDispatcher: CoroutineDispatcher = Dispatchers.IO
        private set
    internal var debugConfig: DebugConfig = DebugConfig.Default
        private set

    /**
     * Sets the dispatchers used in [EiffelViewModel]. Mainly useful for testing.
     *
     * @param[actionDispatcher] [CoroutineDispatcher] used for processing actions, defaults to [Dispatchers.Default].
     * @param[interceptionDispatcher] [CoroutineDispatcher] used for running interceptions, defaults to [Dispatchers.IO].
     */
    fun setDispatchers(actionDispatcher: CoroutineDispatcher = Dispatchers.Default, interceptionDispatcher: CoroutineDispatcher = Dispatchers.IO) {
        this.actionDispatcher = actionDispatcher
        this.interceptionDispatcher = interceptionDispatcher
    }

    /**
     * Resets the dispatchers used in [EiffelViewModel] to [Dispatchers.Default] for actions and [Dispatchers.IO] for interceptions.
     */
    fun resetDispatchers() {
        actionDispatcher = Dispatchers.Default
        interceptionDispatcher = Dispatchers.IO
    }

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
