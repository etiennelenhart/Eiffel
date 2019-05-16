package com.etiennelenhart.eiffel.state

import android.os.Bundle
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Interface for data classes that represent state.
 *
 * Only use `val`s and immutable data structures for properties and try
 * to avoid view specifics like resource IDs or data binding logic.
 * Implementing classes can be used as state in [EiffelViewModel].
 *
 * To automatically receive arguments from the corresponding Activity or Fragment,
 * simply add a secondary constructor matching the arguments' type.
 *
 * *Tip: Try using [Kotlin Android Extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html#parcelable)
 * to gain `@Parcelize` annotation.*
 *
 * Example:
 * ```
 * data class SampleState(sample: String, other: Boolean, more: Int = 0) {
 *     constructor(arguments: SampleArguments) = this(arguments.sample, arguments.other)
 * }
 *
 * @Parcelize
 * class SampleArguments(val sample: String, val other: Boolean) : Parcelable
 *
 * class SampleFragment : Fragment() {
 *     ...
 *
 *     companion object {
 *         fun instance(sample: String, other: Boolean) = SampleFragment().apply {
 *             arguments = Bundle().apply { putParcelable(Eiffel.KEY_ARGS, SampleArguments(sample, other)) }
 *         }
 *     }
 * }
 * ```
 */
interface State {

    /**
     * Return the part of this state that should be saved for process kills as a [Bundle].
     *
     * Example:
     * ```
     * data class SampleState(sample: String = "") {
     *     override fun save() = bundleOf(KEY_SAMPLE to sample)
     *
     *     override fun restore(bundle: Bundle) = copy(sample = bundle.getString(KEY_SAMPLE)!!)
     *
     *     private companion object {
     *         const val KEY_SAMPLE = "sample"
     *     }
     * }
     * ```
     *
     * @return Part of this state that should be saved.
     */
    fun save() = Bundle(0)

    /**
     * Return this state restored with the given [bundle] after a process kill.
     *
     * Example:
     * ```
     * data class SampleState(sample: String = "") {
     *     override fun save() = bundleOf(KEY_SAMPLE to sample)
     *
     *     override fun restore(bundle: Bundle) = copy(sample = bundle.getString(KEY_SAMPLE)!!)
     *
     *     private companion object {
     *         const val KEY_SAMPLE = "sample"
     *     }
     * }
     * ```
     *
     * @param[bundle] Part of this state that has previously been saved using [save].
     */
    fun restore(bundle: Bundle) = this
}
