package com.etiennelenhart.eiffel.state

import java.util.Arrays

/**
 * Base class for one-off state events that require a view interaction.
 *
 * Can be used as a property in [State]s to signal an event to the observer:
 *
 * ```
 * data class SampleState(..., val event: SampleViewEvent? = null) : State
 * ```
 *
 * Extending classes are best declared as 'sealed':
 *
 * ```
 * sealed class SampleViewEvent : ViewEvent() {
 *     class ShowSample : SampleViewEvent()
 *     class DoNotCare : SampleViewEvent()
 * }
 * ```
 *
 * Observers can then check for unhandled events like this:
 *
 * ```
 * viewModel.observeState(this) { state ->
 *     state.event?.peek {
 *         when (it) {
 *             is SampleViewEvent.ShowSample -> {
 *                 ...
 *                 true
 *             }
 *             else -> false
 *         }
 *     }
 * }
 * ```
 */
abstract class ViewEvent {

    internal var handled = false

    final override fun equals(other: Any?): Boolean {
        if (other !is ViewEvent) return false

        return other::class == this::class && other.handled == handled
    }

    final override fun hashCode(): Int = Arrays.hashCode(arrayOf(this::class, handled))
}

/**
 * Allows observers to look at the event's type and decide if they can handle it.
 *
 * @param[handled] Lambda expression that is called with an unhandled event. Should return `true` if the event
 * was handled and `false` if it was ignored or could not be handled.
 */
fun <T : ViewEvent> T.peek(handled: (event: T) -> Boolean) {
    if (!this.handled) this.handled = handled(this)
}
