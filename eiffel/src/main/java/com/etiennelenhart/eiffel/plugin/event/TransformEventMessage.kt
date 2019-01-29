package com.etiennelenhart.eiffel.plugin.event

import com.etiennelenhart.eiffel.plugin.event.TransformEventMessage.Default
import com.etiennelenhart.eiffel.plugin.event.TransformEventMessage.Default.defaultStringTransformer
import com.etiennelenhart.eiffel.plugin.event.TransformEventMessage.Default.toString
import com.etiennelenhart.eiffel.plugin.logger.LoggerPlugin
import com.etiennelenhart.eiffel.state.State

/**
 * Used to customize how an [Event] is transformed for use with a plugin.
 *
 * For example, the [LoggerPlugin] requires the [Event] to be converted to a string.  There is a
 * default transformer [Default], which transforms them into a string for the log.
 *
 * Say you had a plugin that needed the [Event]'s as a JSON object.  Well you could create a
 * [TransformEventMessage] that uses a library to convert it to JSON (like moshi or GSON).
 *
 * @param[T] Type to convert the [Event] to.
 */
interface TransformEventMessage<T> {

    /**
     * Transformer lambda that [Event] into [T].
     */
    val transformer: (Event) -> T

    /**
     * Default implementation of a [TransformEventMessage] that transforms [Event] to [String].
     *
     * Warning: Some [Event]'s contain a [State] reference.  If that has a lot of complex data,
     * it may pollute the logcat. You can trim some of the clutter by overriding the [toString] of the [State].
     *
     * @see defaultStringTransformer
     */
    object Default : TransformEventMessage<String> {
        override val transformer: (Event) -> String = { defaultStringTransformer(it) }
    }

    /**
     * A default transformer that takes an [Event] and returns a [LoggerPlugin] appropriate [String].
     *
     * @param[event] Event to transform.
     */
    fun defaultStringTransformer(event: Event): String = when (event) {
        is Event.Message -> "Received message: ${event.message}"
        is Event.Custom<*> -> event.data.toString()
        is Event.ViewModelCreated<*> -> "Created ${event.name} - Initial state: ${event.initialState}"
        is Event.ViewModelCleared -> "Cleared ${event.name}"
        is Event.Action<*> -> "Dispatching action: ${event.action}"
        is Event.Update<*> -> """
            ->
            State Update:
                Previous: ${event.previous}
                Updated: ${event.updated}
            """.trimIndent()
        is Event.Interception<*, *, *> -> """
            ->
            Applying ${event.interception::class.java.simpleName}:
                Action: ${event.action}
                State: ${event.currentState}
            """.trimIndent()
    }
}

/**
 * Convenience function for creating a [TransformEventMessage].
 *
 * Scoped to the [TransformEventMessage] so the lambda can access [defaultStringTransformer].
 *
 * @param[transformer] A lambda expression for transforming the [Event].
 */
inline fun <T> transformEvent(
    crossinline transformer: TransformEventMessage<T>.(event: Event) -> T
): TransformEventMessage<T> {
    return object : TransformEventMessage<T> {
        override val transformer: (Event) -> T = { event ->
            transformer(this, event)
        }
    }
}