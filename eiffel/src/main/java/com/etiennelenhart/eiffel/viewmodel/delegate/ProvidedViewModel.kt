package com.etiennelenhart.eiffel.viewmodel.delegate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a [ViewModel] from this Activity's [ViewModelProvider].
 *
 * May be used in a [FragmentActivity] like this:
 * ```
 * val viewModel by ProvidedViewModel(SampleViewModel::class.java)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[viewModelClass] Java class of the provided view model.
 */
class ProvidedViewModel<out T : ViewModel>(private val viewModelClass: Class<T>) : ReadOnlyProperty<FragmentActivity, T> {

    private var value: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) value = ViewModelProviders.of(thisRef).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience inline function to use [ProvidedViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [FragmentActivity] like this:
 * ```
 * val viewModel by providedViewModel<SampleViewModel>()
 * ```
 *
 * @param[T] Type of the provided view model.
 */
inline fun <reified T : ViewModel> providedViewModel() = ProvidedViewModel(T::class.java)
