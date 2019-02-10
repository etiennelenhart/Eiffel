package com.etiennelenhart.eiffel.viewmodel

import android.util.Log
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.interception.Interception
import com.etiennelenhart.eiffel.interception.Next
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.Update
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A [ViewModel] supporting an observable state and dispatching of actions to update this state.
 *
 * By default if [Eiffel.debugMode] is enabled then every instance of [EiffelViewModel] will have
 * its [Update], [Interception] and [Action]'s logged.  To exclude a specific [EiffelViewModel] from
 * this functionality override [excludeFromDebug] and set it to `true`.
 *
 * @param[S] Type of associated [State].
 * @param[A] Type of supported [Action]s.
 * @param[initialState] Initial state to set when view model is created.
 * @param[update] Used to update the state according to an action.
 * @param[interceptions] Chain of [Interception] objects to apply to a dispatched [Action].
 * @param[interceptionDispatcher] [CoroutineDispatcher] to use for interception invocation, defaults to [Dispatchers.IO].
 * @param[actionDispatcher] [CoroutineDispatcher] to use for action dispatching, defaults to [Dispatchers.Default]. Mainly used for testing.
 */
abstract class EiffelViewModel<S : State, A : Action>(
    initialState: S,
    private val update: Update<S, A>,
    private val interceptions: List<Interception<S, A>> = emptyList(),
    private val interceptionDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val actionDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    /**
     * Set to `true` to exclude this ViewModel from [Eiffel.debugMode].
     *
     * ```
     * // Disable debug mode for a specific ViewModel
     * class MyViewModel : EiffelViewModel(...) {
     *    override val excludeFromDebug: Boolean = true
     * }
     * ```
     */
    protected open val excludeFromDebug: Boolean = false

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob())
    private val _state = MediatorLiveData<S>()
    @UseExperimental(ObsoleteCoroutinesApi::class)
    private val dispatchActor = scope.actor<A>(actionDispatcher, Channel.UNLIMITED) {
        channel.consumeEach { action ->
            val currentState = _state.value!!

            log("┌───── ↘ Processing: $action")
            log("├─ ↓ Current state: $currentState")

            val resultingAction = applyInterceptions(currentState, action)

            when {
                interceptions.isEmpty() -> Unit
                resultingAction == null -> log("├─   ⇷ Blocked")
                else -> log("├─   ← Result:       $resultingAction")
            }

            resultingAction?.let { applyUpdate(currentState, it) }

            log("└──────────────────────────────────────────")
        }
    }

    /**
     * State that may be observed from a [LifecycleOwner] like [FragmentActivity] or [Fragment].
     */
    val state: LiveData<S>
        get() = _state

    init {
        _state.value = initialState
        log("* Creating ${this::class.java.simpleName}, initial state: $initialState")
    }

    private suspend fun applyInterceptions(currentState: S, action: A) = withContext(interceptionDispatcher) {
        if (interceptions.isEmpty()) log("├─ ↡ No interceptions to apply")
        next(0).invoke(scope, currentState, action, ::dispatch)
    }

    private fun next(index: Int): Next<S, A> = if (index == interceptions.size) {
        { _, _, action, _ -> action }
    } else {
        { scope, state, action, dispatch ->
            interceptions[index].run {
                if (index > 0) log("├─   ← Forwarded:    $action")
                log("├─ ↓ Interception:  ${this.debugName}")
                log("├─   → Received:     $action")
                invoke(scope, state, action, dispatch, next(index + 1))
            }
        }
    }

    private suspend fun applyUpdate(currentState: S, action: A) {
        val updatedState = update(currentState, action)
        if (updatedState != currentState) {
            log("├─ ↙ Updated state: $updatedState")
            withContext(Dispatchers.Main) { _state.value = updatedState }
        } else {
            log("├─ ↪ State unchanged, not emitted")
        }
    }

    /**
     * Adds the given [LiveData] as a source to the private state LiveData by calling [MediatorLiveData.addSource].
     *
     * This allows action dispatching from injected LiveData objects:
     *
     * ```
     * addStateSource(sampleLiveData) { SampleAction.UpdateSample(it) }
     * ```
     *
     * @param[V] Type of the source LiveData's value.
     * @param[source] The [LiveData] to add as a source.
     * @param[action] Lambda expression that should return an [Action] to dispatch when [source] notifies a changed value.
     */
    protected fun <V> addStateSource(source: LiveData<V>, action: (value: V) -> A) =
        _state.addSource(source) { dispatch(action(it)) }

    /**
     * Removes the given [LiveData] from the private state LiveData by calling [MediatorLiveData.removeSource].
     *
     * @param[V] Type of the source LiveData's value.
     * @param[source] The [LiveData] to remove.
     */
    protected fun <V> removeStateSource(source: LiveData<V>) = _state.removeSource(source)

    /**
     * Dispatches the given action by queuing it up for being processed by the state [update].
     */
    fun dispatch(action: A) {
        scope.launch(actionDispatcher) {
            log("↗ Dispatching: $action")
            dispatchActor.send(action)
        }
    }

    @UseExperimental(ExperimentalCoroutinesApi::class)
    @CallSuper
    override fun onCleared() {
        super.onCleared()
        log("✝ Destroying ${this::class.java.simpleName}, canceling command scope")
        scope.cancel()
    }

    private fun log(message: String) {
        if (Eiffel.debugConfig.enabled && !excludeFromDebug) {
            Eiffel.debugConfig.logger.log(Log.DEBUG, this::class.java.simpleName, message)
        }
    }
}
