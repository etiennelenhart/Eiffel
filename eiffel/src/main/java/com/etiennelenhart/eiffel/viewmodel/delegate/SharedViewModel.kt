package com.etiennelenhart.eiffel.viewmodel.delegate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a shared [ViewModel] from the [ViewModelProvider]
 * of this [Fragment]s Activity.
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by SharedViewModel(SampleViewModel::class.java)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[viewModelClass] Java class of the provided view model.
 */
class SharedViewModel<out T : ViewModel>(private val viewModelClass: Class<T>) : ReadOnlyProperty<Fragment, T> {

    private var value: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) value = ViewModelProviders.of(thisRef.activity!!).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience inline function to use [SharedViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by sharedViewModel<SampleViewModel>()
 * ```
 *
 * @param[T] Type of the provided view model.
 */
inline fun <reified T : ViewModel> sharedViewModel() = SharedViewModel(T::class.java)
