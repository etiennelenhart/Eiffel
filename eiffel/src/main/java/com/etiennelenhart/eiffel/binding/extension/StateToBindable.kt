package com.etiennelenhart.eiffel.binding.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.etiennelenhart.eiffel.binding.BindableMapping
import com.etiennelenhart.eiffel.binding.BindableState
import com.etiennelenhart.eiffel.state.State

/**
 * Used to map this state to a bindable one that may be used as a binding variable. Automatically calls default constructor of [B] for initial state.
 *
 * Example:
 * ```
 * class SampleFragment : Fragment() {
 *
 *     ...
 *
 *     override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
 *         val binding = DataBindingUtil.inflate<FragmentSampleBinding>(inflater, R.layout.fragment_sample, container, false)
 *
 *         binding.state = viewModel.state.toBindable(SampleStateMapping())
 *         binding.setLifecycleOwner(viewLifecycleOwner)
 *
 *         return binding.root
 *     }
 * }
 * ```
 *
 * @param[S] Type of this [State].
 * @param[B] Type of mapped [BindableState].
 * @param[mapping] [BindableMapping] that maps this state to a bindable one.
 * @return A [LiveData] with values of type [B].
 */
inline fun <S : State, reified B : BindableState> LiveData<S>.toBindable(mapping: BindableMapping<S, B>): LiveData<B> {
    return MediatorLiveData<B>().apply {
        value = B::class.java.newInstance()
        addSource(this@toBindable) { value = mapping(it, value!!) }
    }
}
