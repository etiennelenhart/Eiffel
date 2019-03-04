package com.etiennelenhart.eiffel.view

import android.os.Parcelable
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory

/**
 * Arguments that can be used in [EiffelArgumentFactory] to inject default values to view models. Should be implemented as a parcelable class.
 * Tip: Try using [Kotlin Android Extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html#parcelable) to gain `@Parcelize` annotation.
 *
 * Example:
 * ```
 * @Parcelize
 * class SampleArguments(val id: String) : EiffelArguments
 * ```
 */
interface EiffelArguments : Parcelable
