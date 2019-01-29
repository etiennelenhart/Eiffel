package com.etiennelenhart.eiffel.plugin.event

import com.etiennelenhart.eiffel.plugin.Dispatcher
import com.etiennelenhart.eiffel.plugin.EiffelPlugin
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import com.etiennelenhart.eiffel.interception.Interception as StateInterception
import com.etiennelenhart.eiffel.state.Action as StateAction

/**
 * Events that an [EiffelPlugin] can interact with.
 *
 * Using a sealed class to make it easier when transforming the [Event] with
 * a [TransformEventMessage]. See [TransformEventMessage.defaultStringTransformer] for a default
 * implementation of a transformer.
 *
 * They are dispatched by a [Dispatcher].
 *
 * @param[tag] A String tag to represent what kind of event is being dispatched, useful for logging.
 */
sealed class Event(val tag: String = "") {

    /**
     * An [Event] indicating that a [EiffelViewModel] has been created.
     *
     * @param[name] Class name of the [EiffelViewModel].
     * @param[initialState] The initial state of the ViewModel.
     */
    data class ViewModelCreated<S : State>(val name: String, val initialState: S) : Event()

    /**
     * An [Event] indicating that a [EiffelViewModel]'s [EiffelViewModel.onCleared] has been called.
     *
     * @param[name] Class name of the [EiffelViewModel].
     */
    data class ViewModelCleared(val name: String) : Event()

    /**
     * An [Event] for dispatching a custom message.
     *
     * Useful if the developer wants to log something during development.
     *
     * @param[message] Custom message.
     */
    data class Message(val message: String) : Event("Message")

    /**
     * An [Event] for dispatching a custom object.
     *
     * Ensure you override the [toString] of [data] so that it can be properly converting to a
     * string by the logger.
     *
     * @param[data] Custom object type.
     */
    data class Custom<T : Any>(val data: T) : Event()

    /**
     * An [Event] indicating the updated [Action] being handled by the [EiffelViewModel].
     *
     * @param[action] Dispatched [Action].
     */
    data class Action<A : StateAction>(val action: A) : Event("Action")

    /**
     * An [Event] for tracking updates to the [EiffelViewModel.state].
     *
     * If you have [State] objects that are very large, like a list of complex objects.  You can
     * override the [toString] of your state.  That way you can control what gets logged.
     *
     * @param[previous] The state before the [Update] was called.
     * @param[updated] The updated state.
     */
    data class Update<S : State>(val previous: S, val updated: S) : Event("Update")

    /**
     * An [Event] indicating the current [Interception].
     *
     * @param[currentState] Current state at the time of the [Interception].
     * @param[action] Action that triggered the [Interception].
     * @param[interception] The current active [Interception].
     */
    data class Interception<S : State, A : StateAction, I : StateInterception<S, A>>(
        val currentState: S,
        val action: A,
        val interception: I
    ) : Event("Interception")
}