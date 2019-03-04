package com.etiennelenhart.eiffel.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory

/**
 * Base class for Eiffel-enabled Fragments, extends from [Fragment].
 */
abstract class EiffelFragment : Fragment() {

    /**
     * Used internally to get [EiffelArguments] set with [setEiffelArguments].
     */
    fun <A : EiffelArguments> getEiffelArguments() = arguments?.getParcelable<A>(ARG_EIFFEL)

    /**
     * Adds provided [args] to this fragment's arguments that can be used for [EiffelArgumentFactory].
     */
    fun <A : EiffelArguments> setEiffelArguments(args: A) {
        arguments = (arguments ?: Bundle()).apply { putParcelable(ARG_EIFFEL, args) }
    }

    private companion object {
        const val ARG_EIFFEL = "arg_eiffel"
    }
}
