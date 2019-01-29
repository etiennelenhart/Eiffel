package com.etiennelenhart.eiffel.plugin

import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.plugin.Dispatcher.Default
import com.etiennelenhart.eiffel.plugin.event.Event
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.Update
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import com.etiennelenhart.eiffel.viewmodel.name

/**
 * Used to customize how the [Event]'s are dispatched to the [EiffelPlugin].
 *
 * [Eiffel.debugMode] defaults to using [Default].
 */
interface Dispatcher {

    /**
     * Dispatch an event to each of the [EiffelPlugin].
     *
     * @see EiffelViewModel.dispatchEiffelEvent
     * @param[dispatcher] String identifier tag for the class that dispatched the event.
     * @param[event] The event to dispatch.
     */
    fun <E : Event> dispatchEvent(dispatcher: String, event: E)

    /**
     * A default [Dispatcher] used by [Eiffel.debugConfig].
     */
    object Default : Dispatcher {

        /**
         * Dispatches the event to all of the registered plugins.
         *
         * TODO - This may not be the best approach, maybe figure out a way not to use the static variable
         *
         * @see Dispatcher.dispatchEvent
         */
        override fun <E : Event> dispatchEvent(dispatcher: String, event: E) {
            Eiffel.debugConfig.plugins.forEach { it.onEvent(dispatcher, event) }
        }
    }
}

/**
 * Dispatch a custom message to all the plugins.
 *
 * @param[message] Custom message to dispatch.
 */
fun <S : State, A : Action> EiffelViewModel<S, A>.dispatchEiffelMessage(message: String) =
    dispatchEiffelEvent(Event.Message(message))

/**
 * Dispatch a custom object to all the plugins.
 *
 * Ensure you override [data]'s [toString] so it can be logged properly.
 *
 * @param[data] Custom object to dispatch.
 */
fun <S : State, A : Action, T : Any> EiffelViewModel<S, A>.dispatchEiffelCustom(data: T) =
    dispatchEiffelEvent(Event.Custom(data))

/**
 * Dispatches a [Event.ViewModelCreated] event with the class name of the updated [EiffelViewModel].
 */
internal fun <S : State, A : Action> EiffelViewModel<S, A>.dispatchEiffelCreated() =
    dispatchEiffelEvent(Event.ViewModelCreated(name, state.value!!))

/**
 * Dispatches a [Event.Action] with the [Action].
 *
 * @param[action] The updated action dispatched by [EiffelViewModel.dispatchActor].
 */
internal fun <S : State, A : Action, T : A> EiffelViewModel<S, A>.dispatchEiffelAction(action: T) =
    dispatchEiffelEvent(Event.Action(action))

/**
 * Dispatches a [Event.Update] with the previous and updated [State].
 *
 * @param[previous] The [State] before the [Update].
 * @param[updated] The [State] after the [Update].
 */
internal fun <S : State, A : Action> EiffelViewModel<S, A>.dispatchEiffelUpdate(previous: S, updated: S) =
    dispatchEiffelEvent(Event.Update(previous, updated))

/**
 * Dispatches a [Event.Interception] with the updated [State], [Action] and [Interception].
 *
 * @param[state] Current state of the [EiffelViewModel].
 * @param[action] Current [Action] that the [Interception] is handling.
 * @param[interception] The interception.
 */
internal fun <S : State, A : Action, I : Interception<S, A>> EiffelViewModel<S, A>.dispatchEiffelInterception(
    state: S,
    action: A,
    interception: I
) =
    dispatchEiffelEvent(Event.Interception(state, action, interception))

/**
 * Dispatches a [Event.ViewModelCleared].
 */
internal fun <S : State, A : Action> EiffelViewModel<S, A>.dispatchEiffelCleared() =
    dispatchEiffelEvent(Event.ViewModelCleared(name))

/**
 * Convenience function for dispatching an event from a [EiffelViewModel].
 *
 * It will only dispatch the event if the user has enabled global debug mode in their app, OR the
 * [EiffelViewModel] has overridden the [EiffelViewModel.debug].
 *
 * @param[event] The [Event] to dispatch to the list of [EiffelPlugin].
 */
private fun <S : State, A : Action, E : Event> EiffelViewModel<S, A>.dispatchEiffelEvent(event: E) {
    if (Eiffel.debugConfig.enabled || debug) {
        Eiffel.debugConfig.dispatcher.dispatchEvent(name, event)
    }
}