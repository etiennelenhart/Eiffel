package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a [ViewModel] from this Fragment's [ViewModelProvider].
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by ProvidedFragmentViewModel(SampleViewModel::class.java)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[viewModelClass] Java class of the provided view model.
 */
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
class ProvidedFragmentViewModel<out T : ViewModel>(private val viewModelClass: Class<T>) : ReadOnlyProperty<Fragment, T> {

    private var value: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) value = ViewModelProviders.of(thisRef).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience extension function to use [ProvidedFragmentViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by providedViewModel<SampleViewModel>()
 * ```
 *
 * @param[T] Type of the provided view model.
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
inline fun <reified T : ViewModel> Fragment.providedViewModel() = ProvidedFragmentViewModel(T::class.java)
