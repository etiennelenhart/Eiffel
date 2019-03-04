package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
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
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Will be removed in next major version, try migrating to EiffelViewModel.")
inline fun <reified T : ViewModel> Fragment.sharedViewModel() = SharedViewModel(T::class.java)
