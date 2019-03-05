package com.etiennelenhart.eiffel.view

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory

/**
 * Base class for Eiffel-enabled Activities, extends from [AppCompatActivity].
 */
abstract class EiffelActivity : AppCompatActivity() {

    /**
     * Used internally to get arguments set with [putEiffelExtra].
     */
    fun getEiffelExtra() = intent.extras?.get(ARG_EIFFEL)

    /**
     * Adds provided [extra] to the extras of the given [intent] which can be used for [EiffelArgumentFactory].
     *
     * Tip: Try using [Kotlin Android Extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html#parcelable) to gain `@Parcelize` annotation.
     *
     * Example:
     * ```
     * @Parcelize
     * class SampleArguments(val id: String) : Parcelable
     * ```
     *
     * @param[E] Type of parcelable extra.
     * @param[intent] [Intent] to put the [extra] into.
     * @param[extra] Extra to put into the [intent].
     */
    fun <E : Parcelable> putEiffelExtra(intent: Intent, extra: E) = intent.putExtra(ARG_EIFFEL, extra)

    private companion object {
        const val ARG_EIFFEL = "arg_eiffel"
    }
}
