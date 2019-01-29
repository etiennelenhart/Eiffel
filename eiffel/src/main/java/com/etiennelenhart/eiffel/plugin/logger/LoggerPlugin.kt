package com.etiennelenhart.eiffel.plugin.logger

import android.util.Log
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.plugin.EiffelPlugin
import com.etiennelenhart.eiffel.plugin.event.Event
import com.etiennelenhart.eiffel.plugin.event.TransformEventMessage
import com.etiennelenhart.eiffel.plugin.event.transformEvent
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * [EiffelPlugin] contract for creating a plugin to log [Event]'s to the logcat.
 *
 * @see DefaultLoggerPlugin
 * @see ReleaseLoggerPlugin
 */
interface LoggerPlugin<T> : EiffelPlugin {

    override val name: String
        get() = "Logger"

    val logger: Logger
    val transform: TransformEventMessage<T>
}

/**
 * A included [LoggerPlugin] for logging all of the [Event]'s to the logcat.
 *
 * [Eiffel.debugConfig] by default will include a [DefaultLoggerPlugin].  If you happen to pass in more
 * plugins via [Eiffel.debugMode], then a [DefaultLoggerPlugin] will be added automatically.  If
 * you have manually supplied a [LoggerPlugin] then only yours will be added.
 *
 * By default it uses the [Logger.Default] which uses [Log.println].  You can pass in a custom
 * logging solution by supplying a [logger].
 *
 * You can also override the default behaviour of turning the [Event] into a string by passing
 * in a [transform].  Say you wanted to export the event as a JSON string you could
 * extend [TransformEventMessage] to create a POJO, then serialize it JSON using a library.
 *
 * @sample
 * ```
 * class MyApp : Application {
 *
 *   override onCreate() {
 *      super.onCreate()
 *
 *      Eiffel.debugMode(true, listOf(
 *          DefaultLoggerPlugin(
 *             logger = log { priority, tag, message ->
 *                Timber.tag(tag).log(priority, message)
 *             }
 *         )
 *      ))
 *   }
 * }
 * ```
 *
 * @see Logger
 * @see log
 * @see TransformEventMessage.Default
 * @param[logger] Logger implementation for logging the events
 * @param[transform] TransformEventMessage implementation for transforming events into String's
 */
class DefaultLoggerPlugin(
    override val logger: Logger = Logger.Default,
    override val transform: TransformEventMessage<String> = TransformEventMessage.Default
) : LoggerPlugin<String> {

    override fun <E : Event> onEvent(dispatcher: String, event: E) {
        val eventMessage = transform.transformer(event)

        val tag = if (event.tag.isEmpty()) "" else ":${event.tag}"
        logger.log(Log.DEBUG, "Eiffel-$name:$dispatcher$tag", eventMessage)
    }
}

/**
 * A included [LoggerPlugin] for use with a release flavour of an app.
 *
 * It will not transform or log any output, use this if you would like to disable all
 * logging in production. Use in production to ensure no logging will happen. As uou can
 * disable [Eiffel]'s debug mode, but a [EiffelViewModel] might accidentally
 * have overridden [EiffelViewModel.debug].
 *
 * @sample
 * ```
 * class MyReleaseApp : Application {
 *
 *   override onCreate() {
 *      super.onCreate()
 *
 *      Eiffel.debugMode(false, listOf(ReleaseLoggerPlugin))
 *   }
 * }
 * ```
 *
 * @see DefaultLoggerPlugin
 * @see Eiffel.debugMode
 */
object ReleaseLoggerPlugin : LoggerPlugin<Unit> {

    override val logger: Logger = Logger.ReleaseLogger
    override val transform: TransformEventMessage<Unit> = transformEvent { }

    override fun <E : Event> onEvent(dispatcher: String, event: E) {
        // noop
    }
}


