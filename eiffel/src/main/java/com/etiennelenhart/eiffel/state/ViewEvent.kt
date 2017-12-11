package com.etiennelenhart.eiffel.state

/**
 * Base class for view model actions that require an Activity.
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
 *     class ActivityAction : SampleViewEvent()
 * }
 * ```
 *
 * Observers can then check for pending events like this:
 *
 * ```
 * viewModel.state.observe(this, Observer {
 *     if (!it.event.handled) {
 *         when (it.event) {
 *             is SampleViewEvent.ActivityAction -> {
 *                 it.event.handled = true
 *                 ...
 *             }
 *         }
 *     }
 * })
 * ```
 *
 * @param[handled] 'true' when the event is already handled. Defaults to false.
 * @property[handled] 'false' when the event has yet to be handled. Set to 'true' when event is handled.
 */
abstract class ViewEvent(var handled: Boolean = false) {
    /**
     * Convenience [ViewEvent] to set as an initial event that requires no action.
     *
     * This event's 'handled' property is initialized to 'true'.
     */
    object None : ViewEvent(true)
}
