package com.etiennelenhart.eiffel.interception

import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * Lambda expression representing the next [Interception] item in a chain.
 *
 * @param[State] Type of [State] to receive.
 * @param[Action] Type of supported [Action].
 */
typealias Next<State, Action> = suspend (scope: CoroutineScope, state: State, action: Action, dispatch: (Action) -> Unit) -> Action

/**
 * May be used as an item in the [EiffelViewModel.interceptions] chain to intercept dispatching of an [Action].
 *
 * @param[S] Type of [State] to receive.
 * @param[A] Type of supported [Action].
 */
interface Interception<S : State, A : Action> {

    /**
     * When an [Action] is dispatched using [EiffelViewModel.dispatch], the first item in the [EiffelViewModel.interceptions] chain is invoked.
     * It is then up to the [Interception] to decide how to react to the [Action]:
     *  * Ignore it and pass it to the next chain item by forwarding it to [next].
     *  * Do some unrelated work, like logging and forward it to [next]. (see [Pipe])
     *  * Adapt it and pass it to the next chain item by forwarding the updated [Action] to [next]. (see [Adapter])
     *  * Block it from passing through the chain by simply returning the received [Action] and not calling [next]. (see [Filter])
     *  * Consume it by returning a new [Action] and not calling [next].
     * It is also possible to asynchronously call [dispatch] at any time which will dispatch the provided [Action] using [EiffelViewModel.dispatch],
     * effectively invoking the [EiffelViewModel.interceptions] chain again.
     *
     * Example:
     * ```
     * class SampleInterception(private val sampleCommand: suspend (index: Int) -> String) : Interception<SampleState, SampleAction> {
     *
     *     override suspend fun invoke(
     *         scope: CoroutineScope,
     *         state: SampleState,
     *         action: SampleAction,
     *         dispatch: (SampleAction) -> Unit,
     *         next: Next<SampleState, SampleAction>
     *     ) = when(action) {
     *         is SampleAction.DoSample -> {
     *             scope.launch {
     *                 val sample = sampleCommand(action.index)
     *                 dispatch(SampleAction.UpdateSample(sample))
     *             }
     *             SampleAction.DoingSample
     *         }
     *         else -> next(scope, state, action, dispatch)
     *     }
     * }
     * ```
     *
     * @param[scope] [CoroutineScope] that can be used to start coroutines scoped to the corresponding [EiffelViewModel]'s lifecycle. Call [dispatch] from such
     * coroutines to update state.
     * @param[state] Value of the [State] at the time of dispatching the [action].
     * @param[action] The dispatched [Action].
     * @param[dispatch] Call this asynchronously to dispatch an [Action] at any time.
     * @param[next] Represents the next item in the chain.
     * @return The [Action] to pass back to the original [EiffelViewModel.dispatch] operation.
     */
    suspend operator fun invoke(scope: CoroutineScope, state: S, action: A, dispatch: (action: A) -> Unit, next: Next<S, A>): A
}
