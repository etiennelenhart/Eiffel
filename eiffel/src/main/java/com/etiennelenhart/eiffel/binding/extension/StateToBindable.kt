package com.etiennelenhart.eiffel.binding.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.etiennelenhart.eiffel.binding.BindableMapping1
import com.etiennelenhart.eiffel.binding.BindableMapping2
import com.etiennelenhart.eiffel.binding.BindableMapping3
import com.etiennelenhart.eiffel.binding.BindableState
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.view.EiffelFragment

/**
 * Used to map this [State] to a [BindableState] that may be used as a binding variable. Automatically calls default constructor of [B] for initial state.
 *
 * Example:
 * ```
 * class SampleFragment : EiffelFragment() {
 *
 *     ...
 *     val bindableState by lazy { viewModel.state.toBindable(SampleStateMapping()) }
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val binding = DataBindingUtil.inflate<FragmentSampleBinding>(inflater, R.layout.fragment_sample, container, false)
 *         binding.view = this
 *         binding.setLifecycleOwner(viewLifecycleOwner)
 *         return binding.root
 *     }
 * }
 * ```
 *
 * @param[S] Type of this [State].
 * @param[B] Type of mapped [BindableState].
 * @param[mapping] [BindableMapping1] that maps this state to a bindable one.
 * @return A [LiveData] with values of type [B].
 */
inline fun <S : State, reified B : BindableState> LiveData<S>.toBindable(mapping: BindableMapping1<S, B>): LiveData<B> {
    return MediatorLiveData<B>().apply {
        value = B::class.java.newInstance()
        addSource(this@toBindable) { value = mapping(it, value!!) }
    }
}

/**
 * Encapsulates two [State] sources for mapping to a [BindableState].
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[firstState] First [State] source.
 * @param[secondState] Second [State] source.
 */
class DoubleBindableStateSource<S1 : State, S2 : State>(val firstState: LiveData<S1>, val secondState: LiveData<S2>)

/**
 * Encapsulates three [State] sources for mapping to a [BindableState].
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[S3] Type of third [State].
 * @param[firstState] First [State] source.
 * @param[secondState] Second [State] source.
 * @param[thirdState] Third [State] source.
 */
class TripleBindableStateSource<S1 : State, S2 : State, S3 : State>(val firstState: LiveData<S1>, val secondState: LiveData<S2>, val thirdState: LiveData<S3>)

/**
 * Convenience builder function to create a [DoubleBindableStateSource] for mapping two [State] sources to a [BindableState].
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[firstState] First [State] source.
 * @param[secondState] Second [State] source.
 */
fun <S1 : State, S2 : State> EiffelFragment.bindableStateSource(firstState: LiveData<S1>, secondState: LiveData<S2>) =
    DoubleBindableStateSource(firstState, secondState)

/**
 * Convenience builder function to create a [TripleBindableStateSource] for mapping three [State] sources to a [BindableState].
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[S3] Type of third [State].
 * @param[firstState] First [State] source.
 * @param[secondState] Second [State] source.
 * @param[thirdState] Third [State] source.
 */
fun <S1 : State, S2 : State, S3 : State> EiffelFragment.bindableStateSource(firstState: LiveData<S1>, secondState: LiveData<S2>, thirdState: LiveData<S3>) =
    TripleBindableStateSource(firstState, secondState, thirdState)

/**
 * Used to map both [State] sources to a [BindableState] that may be used as a binding variable. Automatically calls default
 * constructor of [B] for initial state.
 *
 * Example:
 * ```
 * class SampleFragment : EiffelFragment() {
 *
 *     ...
 *     val bindableState by lazy { bindableStateSource(activityViewModel.state, viewModel.state).toBindable(bindableSampleMapping()) }
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val binding = DataBindingUtil.inflate<FragmentSampleBinding>(inflater, R.layout.fragment_sample, container, false)
 *         binding.view = this
 *         binding.setLifecycleOwner(viewLifecycleOwner)
 *         return binding.root
 *     }
 * }
 * ```
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[B] Type of mapped [BindableState].
 * @param[mapping] [BindableMapping1] that maps this state to a bindable one.
 * @return A [LiveData] with values of type [B].
 */
inline fun <S1 : State, S2 : State, reified B : BindableState> DoubleBindableStateSource<S1, S2>.toBindable(mapping: BindableMapping2<S1, S2, B>): LiveData<B> {
    return MediatorLiveData<B>().apply {
        value = B::class.java.newInstance()
        addSource(this@toBindable.firstState) { value = mapping(it, secondState.value!!, value!!) }
        addSource(this@toBindable.secondState) { value = mapping(firstState.value!!, it, value!!) }
    }
}

/**
 * Used to map all three [State] sources to a [BindableState] that may be used as a binding variable. Automatically calls default
 * constructor of [B] for initial state.
 *
 * Example:
 * ```
 * class SampleFragment : EiffelFragment() {
 *
 *     ...
 *     val bindableState by lazy { bindableStateSource(activityViewModel.state, parentViewModel.state, viewModel.state).toBindable(bindableSampleMapping()) }
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val binding = DataBindingUtil.inflate<FragmentSampleBinding>(inflater, R.layout.fragment_sample, container, false)
 *         binding.view = this
 *         binding.setLifecycleOwner(viewLifecycleOwner)
 *         return binding.root
 *     }
 * }
 * ```
 *
 * @param[S1] Type of first [State].
 * @param[S2] Type of second [State].
 * @param[S3] Type of third [State].
 * @param[B] Type of mapped [BindableState].
 * @param[mapping] [BindableMapping1] that maps this state to a bindable one.
 * @return A [LiveData] with values of type [B].
 */
inline fun <S1 : State, S2 : State, S3 : State, reified B : BindableState> TripleBindableStateSource<S1, S2, S3>.toBindable(
    mapping: BindableMapping3<S1, S2, S3, B>
): LiveData<B> {
    return MediatorLiveData<B>().apply {
        value = B::class.java.newInstance()
        addSource(this@toBindable.firstState) { value = mapping(it, secondState.value!!, thirdState.value!!, value!!) }
        addSource(this@toBindable.secondState) { value = mapping(firstState.value!!, it, thirdState.value!!, value!!) }
        addSource(this@toBindable.thirdState) { value = mapping(firstState.value!!, secondState.value!!, it, value!!) }
    }
}
