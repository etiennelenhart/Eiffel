package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
class ProvidedViewModel<out T : ViewModel>(private val viewModelClass: Class<T>) : ReadOnlyProperty<FragmentActivity, T> {

    private var value: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) value = ViewModelProvider(thisRef).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience extension function to use [ProvidedViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [FragmentActivity] like this:
 * ```
 * val viewModel by providedViewModel<SampleViewModel>()
 * ```
 *
 * @param[T] Type of the provided view model.
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
inline fun <reified T : ViewModel> FragmentActivity.providedViewModel() = ProvidedViewModel(T::class.java)
