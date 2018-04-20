package com.etiennelenhart.eiffel.state

/**
 * Base class for one-off view model actions that require an Activity.
 *
 * Can be used as a property in [ViewState]s to signal an action to the observer:
 *
 * ```
 * data class SampleViewState(..., val event: ViewEvent = ViewEvent.None) : ViewState
 * ```
 *
 * Extending classes are best implemented as 'sealed':
 *
 * ```
 * sealed class SampleViewEvent : ViewEvent() {
 *     class ShowSample : SampleViewEvent()
 * }
 * ```
 *
 * Observers can then check for pending events like this:
 *
 * ```
 * viewModel.state.observe(this, Observer {
 *     when (it.event) {
 *         is SampleViewEvent.ShowSample -> it.event.handle { ... }
 *     }
 * })
 * ```
 */
abstract class ViewEvent {

    protected var handled = false

    /**
     * Marks this event as "handled" and calls the provided lambda expression if it has not been handled already.
     */
    fun handle(block: () -> Unit) {
        if (!handled) {
            handled = true
            block()
        }
    }

    /**
     * Convenience [ViewEvent] to set as an initial event that requires no handling.
     */
    object None : ViewEvent() {

        init {
            handled = true
        }
    }
}
