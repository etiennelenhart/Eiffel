package com.etiennelenhart.eiffel.view

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory

/**
 * Base class for Eiffel-enabled Fragments, extends from [Fragment].
 */
abstract class EiffelFragment : Fragment() {

    /**
     * Used internally to get arguments set with [setEiffelArguments].
     */
    fun getEiffelArguments() = arguments?.get(ARG_EIFFEL)

    /**
     * Adds provided [arguments] to this Fragment's arguments that can be used for [EiffelArgumentFactory].
     *
     * Tip: Try using [Kotlin Android Extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html#parcelable) to gain `@Parcelize` annotation.
     *
     * Example:
     * ```
     * @Parcelize
     * class SampleArguments(val id: String) : Parcelable
     * ```
     *
     * @param[A] Type of parcelable arguments.
     * @param[arguments] Arguments to add.
     */
    fun <A : Parcelable> setEiffelArguments(arguments: A) {
        this.arguments = (this.arguments ?: Bundle()).apply { putParcelable(ARG_EIFFEL, arguments) }
    }

    private companion object {
        const val ARG_EIFFEL = "arg_eiffel"
    }
}
