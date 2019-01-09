package com.etiennelenhart.eiffel.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.etiennelenhart.eiffel.action.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.state.Update
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach

/**
 * A [ViewModel] supporting an observable state and dispatching of actions to update this state.
 *
 * @param[S] Type of associated state.
 * @param[A] Type of supported actions.
 * @param[initialState] Initial state to set when view model is created.
 * @param[update] Function to update the state according to the given action.
 * @param[dispatcher] [CoroutineDispatcher] to use for action dispatching, defaults to [Dispatchers.Default]. Mainly used for testing.
 */
abstract class EiffelViewModel<S : State, A : Action>(
        initialState: S,
        private val update: Update<S, A>,
        dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val state = MediatorLiveData<S>()

    @UseExperimental(ObsoleteCoroutinesApi::class)
    private val dispatchActor = scope.actor<A>(capacity = Channel.UNLIMITED) {
        channel.consumeEach { action ->
            state.value!!.let { currentState ->
                val updatedState = update(currentState, action)
                if (updatedState != currentState) state.value = updatedState
            }
        }
    }

    init {
        state.value = initialState
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
    protected fun <V> addStateSource(source: LiveData<V>, action: (value: V) -> A) = state.addSource(source) { dispatch(action(it)) }

    /**
     * Removes the given [LiveData] from the private state LiveData by calling [MediatorLiveData.removeSource].
     *
     * @param[V] Type of the source LiveData's value.
     * @param[source] The [LiveData] to remove.
     */
    protected fun <V> removeStateSource(source: LiveData<V>) = state.removeSource(source)

    /**
     * Dispatches the given action by queuing it up for being processed by the state [update].
     */
    fun dispatch(action: A) = scope.launch { dispatchActor.send(action) }

    /**
     * Used to observe this [EiffelViewModel]'s state from a [LifecycleOwner] like [FragmentActivity] or [Fragment].
     *
     * @param[owner] [LifecycleOwner] that controls observation.
     * @param[onChanged] Lambda expression that is called with an updated state.
     */
    fun observeState(owner: LifecycleOwner, onChanged: (state: S) -> Unit) = state.observe(owner, Observer { onChanged(it!!) })

    internal fun observeStateForever(onChanged: (state: S) -> Unit) = state.observeForever(onChanged)

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
