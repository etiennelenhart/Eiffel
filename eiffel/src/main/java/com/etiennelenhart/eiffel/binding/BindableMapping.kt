package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State

/**
 * Maps a received [State] to a [BindableState].
 *
 * Example:
 * ```
 * class SampleStateMapping : BindableMapping<SampleState, BindableSampleState>() {
 *     override fun BindableSampleState.map(state: SampleState) = copy(
 *         requiredHintVisible = state.sample.isBlank()
 *     )
 * }
 * ```
 *
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 */
abstract class BindableMapping<S : State, B : BindableState> {

    /**
     * Map the received state to a [BindableState].
     *
     * It's recommended to use `copy` here to keep performance impact minimal.
     *
     * @receiver[B] Current value of the mapped [BindableState].
     * @return [BindingState] mapped from the received state.
     */
    protected abstract fun B.map(state: S): B

    operator fun invoke(state: S, currentBindableState: B) = currentBindableState.map(state)
}

/**
 * Convenience builder function that returns an object extending [BindableMapping]. Passes provided lambda to overridden function.
 *
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[map] Lambda expression to map the received state to a [BindableState]. (see [BindableMapping.map])
 * @return An object extending [BindableMapping].
 */
fun <S : State, B : BindableState> bindableMapping(map: B.(state: S) -> B): BindableMapping<S, B> {
    return object : BindableMapping<S, B>() {
        override fun B.map(state: S) = map(state)
    }
}
