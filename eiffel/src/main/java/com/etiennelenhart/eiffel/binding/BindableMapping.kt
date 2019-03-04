package com.etiennelenhart.eiffel.binding

import com.etiennelenhart.eiffel.state.State

/**
 * Maps a received [State] to a [BindableState].
 *
 * Example:
 * ```
 * class BindableSampleMapping : BindableMapping1<SampleState, BindableSampleState>() {
 *     override fun BindableSampleState.map(state: SampleState) = copy(
 *         requiredHintVisible = state.sample.isBlank()
 *     )
 * }
 * ```
 *
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 */
abstract class BindableMapping1<S : State, B : BindableState> {

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
 * Convenience builder function that returns an object extending [BindableMapping1]. Passes provided lambda to overridden function.
 *
 * Example:
 * ```
 * fun bindableSampleMapping() = bindableMapping<SampleState, BindableSampleState> { state ->
 *     copy(requiredHintVisible = state.sample.isBlank())
 * }
 * ```
 * @param[S] Type of received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[map] Lambda expression to map the received state to a [BindableState]. (see [BindableMapping1.map])
 * @return An object extending [BindableMapping1].
 */
fun <S : State, B : BindableState> bindableMapping(map: B.(state: S) -> B): BindableMapping1<S, B> {
    return object : BindableMapping1<S, B>() {
        override fun B.map(state: S) = map(state)
    }
}

/**
 * Maps two received [State] instances to a [BindableState].
 *
 * Example:
 * ```
 * class BindableSampleMapping : BindableMapping2<FirstSampleState, SecondSampleState, BindableSampleState>() {
 *     override fun BindableSampleState.map(firstState: FirstSampleState, secondState: SecondSampleState) = copy(
 *         requiredHintVisible = firstState.sample1.isBlank() && secondState.sample2.isBlank()
 *     )
 * }
 * ```
 *
 * @param[S1] Type of first received [State].
 * @param[S2] Type of second received [State].
 * @param[B] Type of [BindableState] to map to.
 */
abstract class BindableMapping2<S1 : State, S2 : State, B : BindableState> {

    /**
     * Map both received states to a [BindableState].
     *
     * It's recommended to use `copy` here to keep performance impact minimal.
     *
     * @receiver[B] Current value of the mapped [BindableState].
     * @return [BindingState] mapped from the received state.
     */
    protected abstract fun B.map(firstState: S1, secondState: S2): B

    operator fun invoke(firstState: S1, secondState: S2, currentBindableState: B) = currentBindableState.map(firstState, secondState)
}

/**
 * Convenience builder function that returns an object extending [BindableMapping2]. Passes provided lambda to overridden function.
 *
 * Example:
 * ```
 * fun bindableSampleMapping() = bindableMapping<FirstSampleState, SecondSampleState, BindableSampleState> { firstState, secondState ->
 *     copy(requiredHintVisible = firstState.sample1.isBlank() && secondState.sample2.isBlank()
 * }
 * ```
 * @param[S1] Type of first received [State].
 * @param[S2] Type of second received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[map] Lambda expression to map the received states to a [BindableState]. (see [BindableMapping2.map])
 * @return An object extending [BindableMapping2].
 */
fun <S1 : State, S2 : State, B : BindableState> bindableMapping(map: B.(firstState: S1, secondState: S2) -> B): BindableMapping2<S1, S2, B> {
    return object : BindableMapping2<S1, S2, B>() {
        override fun B.map(firstState: S1, secondState: S2) = map(firstState, secondState)
    }
}

/**
 * Maps three received [State] instances to a [BindableState].
 *
 * Example:
 * ```
 * class BindableSampleMapping : BindableMapping3<FirstSampleState, SecondSampleState, ThirdSampleState, BindableSampleState>() {
 *     override fun BindableSampleState.map(firstState: FirstSampleState, secondState: SecondSampleState, thirdState: ThirdSampleState) = copy(
 *         requiredHintVisible = firstState.sample1.isBlank() && secondState.sample2.isBlank() && thirdState.sample3.isBlank()
 *     )
 * }
 * ```
 *
 * @param[S1] Type of first received [State].
 * @param[S2] Type of second received [State].
 * @param[S3] Type of third received [State].
 * @param[B] Type of [BindableState] to map to.
 */
abstract class BindableMapping3<S1 : State, S2 : State, S3 : State, B : BindableState> {

    /**
     * Map all three received states to a [BindableState].
     *
     * It's recommended to use `copy` here to keep performance impact minimal.
     *
     * @receiver[B] Current value of the mapped [BindableState].
     * @return [BindingState] mapped from the received state.
     */
    protected abstract fun B.map(firstState: S1, secondState: S2, thirdState: S3): B

    operator fun invoke(firstState: S1, secondState: S2, thirdState: S3, currentBindableState: B) =
        currentBindableState.map(firstState, secondState, thirdState)
}

/**
 * Convenience builder function that returns an object extending [BindableMapping3]. Passes provided lambda to overridden function.
 *
 * Example:
 * ```
 * fun bindableSampleMapping() = bindableMapping<FirstSampleState, SecondSampleState, ThirdSampleState, BindableSampleState> { firstState, secondState, thirdState ->
 *     copy(requiredHintVisible = firstState.sample1.isBlank() && secondState.sample2.isBlank() && thirdState.sample3.isBlank())
 * }
 * ```
 * @param[S1] Type of first received [State].
 * @param[S2] Type of second received [State].
 * @param[S3] Type of third received [State].
 * @param[B] Type of [BindableState] to map to.
 * @param[map] Lambda expression to map the received states to a [BindableState]. (see [BindableMapping3.map])
 * @return An object extending [BindableMapping3].
 */
fun <S1 : State, S2 : State, S3 : State, B : BindableState> bindableMapping(
    map: B.(firstState: S1, secondState: S2, thirdState: S3) -> B
): BindableMapping3<S1, S2, S3, B> {
    return object : BindableMapping3<S1, S2, S3, B>() {
        override fun B.map(firstState: S1, secondState: S2, thirdState: S3) = map(firstState, secondState, thirdState)
    }
}
