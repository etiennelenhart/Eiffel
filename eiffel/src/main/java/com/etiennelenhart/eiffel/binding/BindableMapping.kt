package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State

/**
 * Maps a received [State] to a [BindableState].
 *
 * Example:
 * ```
 * class SampleStateMapping : BindableMapping<SampleState, BindableSampleState>(BindableSampleState()) {
 *     override fun BindableSampleState.map(state: SampleState) = copy(
 *         requiredHintVisible = state.sample.isBlank()
 *     )
 * }
 * ```
 *
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[initialState] Initial value of the [BindableState] used as initial receiver for [map].
 */
abstract class BindableMapping<S : State, B : BindableState>(private val initialState: B) {

    /**
     * Lambda expression to map the received state to a [BindableState].
     *
     * It's recommended to use `copy` here to keep performance impact minimal.
     *
     * @receiver[B] Current value of the mapped [BindableState], initially [initialState].
     * @return [BindingState] mapped from the received state.
     */
    protected abstract val map: B.(state: S) -> B

    operator fun invoke(state: S, currentBindableState: B?) = (currentBindableState ?: initialState).map(state)
}

/**
 * Convenience builder function that returns an object extending [BindableMapping]. Passes provided lambdas to overridden properties.
 *
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[initialState] Initial value of the [BindableState] used as initial receiver for [map].
 * @param[map] Lambda expression to map the received state to a [BindableState]. (see [BindableMapping.map])
 * @return An object extending [BindableMapping].
 */
fun <S : State, B : BindableState> bindableMapping(initialState: B, map: B.(state: S) -> B): BindableMapping<S, B> {
    return object : BindableMapping<S, B>(initialState) {
        override val map = map
    }
}
