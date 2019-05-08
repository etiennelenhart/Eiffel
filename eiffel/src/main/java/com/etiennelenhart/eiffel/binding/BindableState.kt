package com.etiennelenhart.eiffel.binding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.util.distinctUntilChanged
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Base class for classes which represent state that may be used for data binding.
 *
 * Should expose separate [LiveData] properties which can be referenced in binding expressions by using [bindableProperty] delegate.
 * May contain view specifics like resource IDs and layout logic.
 *
 * Example:
 * ```
 * class BindableSampleState(state: LiveData<SampleState>) : BindableState<SampleState>(state) {
 *     val requiredHintVisible by bindableProperty(false) { it.sample.isBlank() }
 * }
 * ```
 * To use this state in the layout binding simply assign it to the respective variable:
 * ```
 * class SampleFragment : EiffelFragment() {
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val binding = DataBindingUtil.inflate<FragmentSampleBinding>(inflater, R.layout.fragment_sample, container, false)
 *
 *         binding.state = BindableSampleState(viewModel.state)
 *         binding.setLifecycleOwner(viewLifecycleOwner)
 *
 *         return binding.root
 *     }
 * }
 * ```
 * *Note: Don't forget to call `binding.setLifecycleOwner(...)` to enable Data binding's LiveData support.
 * You also may have to use `safeUnbox()` in XML binding expressions.*
 *`
 * @param[S] Type of primary received [State].
 * @param[state] The primary [EiffelViewModel] state to receive.
 */
abstract class BindableState<S : State>(private val state: LiveData<S>) {

    /**
     * Convenience function for the [BindableProperty1] delegate to map the given state to a property which can be referenced in binding expressions.
     *
     * Passes primary [state] value to [map].
     *
     * Example:
     * ```
     * class BindableSampleState(state: LiveData<SampleState>) : BindableState<SampleState>(state) {
     *     val requiredHintVisible by bindableProperty(false) { it.sample.isBlank() }
     * }
     * ```
     *
     * @param[P] Type of resulting property.
     * @param[initialValue] Initial value of the property.
     * @param[map] Lambda expression that should map the received state value to the desired property.
     */
    protected fun <P> bindableProperty(initialValue: P, map: (state: S) -> P) = BindableProperty1(initialValue, state, map)

    /**
     * Convenience function for the [BindableProperty2] delegate to map two given states to a single property which can be referenced in binding expressions.
     *
     * Passes primary [state] value and [secondaryState] value to [map].
     *
     * Example:
     * ```
     * class BindableSampleState(primaryState: LiveData<SampleState>, secondaryState: LiveData<AnotherState>) : BindableState<SampleState>(primaryState) {
     *     val requiredHintVisible by bindableProperty(false, secondaryState) { primary, secondary -> primary.sample.isBlank() && secondary.another.isBlank() }
     * }
     * ```
     *
     * @param[S2] Type of secondary received [State].
     * @param[P] Type of resulting property.
     * @param[initialValue] Initial value of the property.
     * @param[secondaryState] Secondary [EiffelViewModel] state to map.
     * @param[map] Lambda expression that should map the received state values to the desired property.
     */
    protected fun <S2 : State, P> bindableProperty(
        initialValue: P,
        secondaryState: LiveData<S2>,
        map: (primary: S, secondary: S2) -> P
    ) = BindableProperty2(initialValue, state, secondaryState, map)

    /**
     * Convenience function for the [BindableProperty3] delegate to map three given states to a single property which can be referenced in binding expressions.
     *
     * Passes primary [state] value, [secondaryState] value and [tertiaryState] value to [map].
     *
     * Example:
     * ```
     * class BindableSampleState(
     *     primaryState: LiveData<SampleState>,
     *     secondaryState: LiveData<AnotherState>,
     *     tertiaryState: LiveData<ExampleState>
     * ) : BindableState<SampleState>(primaryState) {
     *     val requiredHintVisible by bindableProperty(false, secondaryState, tertiaryState) { primary, secondary, tertiary ->
     *         primary.sample.isBlank() && secondary.another.isBlank() && tertiary.example.isBlank()
     *     }
     * }
     * ```
     *
     * @param[S2] Type of secondary received [State].
     * @param[S3] Type of tertiary received [State].
     * @param[P] Type of resulting property.
     * @param[initialValue] Initial value of the property.
     * @param[secondaryState] Secondary [EiffelViewModel] state to map.
     * @param[tertiaryState] Tertiary [EiffelViewModel] state to map.
     * @param[map] Lambda expression that should map the received state values to the desired property.
     */
    protected fun <S2 : State, S3 : State, P> bindableProperty(
        initialValue: P,
        secondaryState: LiveData<S2>,
        tertiaryState: LiveData<S3>,
        map: (primary: S, secondary: S2, tertiary: S3) -> P
    ) = BindableProperty3(initialValue, state, secondaryState, tertiaryState, map)
}

/**
 * Property delegate to map the given state to a property which can be referenced in binding expressions.
 *
 * @param[S] Type of received [State].
 * @param[P] Type of resulting property.
 * @param[initialValue] Initial value of the property.
 * @param[state] The [EiffelViewModel] state to map.
 * @param[map] Lambda expression that should map the received state values to the desired property.
 */
class BindableProperty1<S : State, P>(initialValue: P, state: LiveData<S>, map: (state: S) -> P) : ReadOnlyProperty<BindableState<S>, LiveData<P>> {

    private val _property = MediatorLiveData<P>().apply {
        value = initialValue
        addSource(state) { value = map(it) }
    }.distinctUntilChanged()

    override fun getValue(thisRef: BindableState<S>, property: KProperty<*>) = _property
}

/**
 * Property delegate to map two given states to a single property which can be referenced in binding expressions.
 *
 * @param[S] Type of primary received [State].
 * @param[S2] Type of secondary received [State].
 * @param[P] Type of resulting property.
 * @param[initialValue] Initial value of the property.
 * @param[primaryState] Primary [EiffelViewModel] state to map.
 * @param[secondaryState] Secondary [EiffelViewModel] state to map.
 * @param[map] Lambda expression that should map the received state values to the desired property.
 */
class BindableProperty2<S : State, S2 : State, P>(
    initialValue: P,
    primaryState: LiveData<S>,
    secondaryState: LiveData<S2>,
    map: (primary: S, secondary: S2) -> P
) : ReadOnlyProperty<BindableState<S>, LiveData<P>> {

    private val _property = MediatorLiveData<P>().apply {
        value = initialValue
        addSource(primaryState) { value = map(it, secondaryState.value!!) }
        addSource(secondaryState) { value = map(primaryState.value!!, it) }
    }.distinctUntilChanged()

    override fun getValue(thisRef: BindableState<S>, property: KProperty<*>) = _property
}

/**
 * Property delegate to map three given states to a single property which can be referenced in binding expressions.
 *
 * @param[S] Type of primary received [State].
 * @param[S2] Type of secondary received [State].
 * @param[S3] Type of tertiary received [State].
 * @param[P] Type of resulting property.
 * @param[initialValue] Initial value of the property.
 * @param[primaryState] Primary [EiffelViewModel] state to map.
 * @param[secondaryState] Secondary [EiffelViewModel] state to map.
 * @param[tertiaryState] Tertiary [EiffelViewModel] state to map.
 * @param[map] Lambda expression that should map the received state values to the desired property.
 */
class BindableProperty3<S : State, S2 : State, S3 : State, P>(
    initialValue: P,
    primaryState: LiveData<S>,
    secondaryState: LiveData<S2>,
    tertiaryState: LiveData<S3>,
    map: (primary: S, secondary: S2, tertiary: S3) -> P
) : ReadOnlyProperty<BindableState<S>, LiveData<P>> {

    private val _property = MediatorLiveData<P>().apply {
        value = initialValue
        addSource(primaryState) { value = map(it, secondaryState.value!!, tertiaryState.value!!) }
        addSource(secondaryState) { value = map(primaryState.value!!, it, tertiaryState.value!!) }
        addSource(tertiaryState) { value = map(primaryState.value!!, secondaryState.value!!, it) }
    }.distinctUntilChanged()

    override fun getValue(thisRef: BindableState<S>, property: KProperty<*>) = _property
}
