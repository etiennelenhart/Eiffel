package com.etiennelenhart.eiffel.viewmodel.delegate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a shared [ViewModel] instantiated by a [ViewModelProvider.Factory]
 * from the [ViewModelProvider] of this [Fragment]s Activity.
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by SharedFactoryViewModel(SampleViewModel::class.java, sampleFactory)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[viewModelClass] Java class of the provided view model.
 * @param[factory] Lambda expression to lazily get the factory to instantiate the view model.
 */
class SharedFactoryViewModel<out T : ViewModel>(
    private val viewModelClass: Class<T>,
    private val factory: () -> ViewModelProvider.Factory
) : ReadOnlyProperty<Fragment, T> {

    private var value: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) value = ViewModelProviders.of(thisRef.activity!!, factory()).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience inline function to use [SharedFactoryViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [Fragment] like this:
 * ```
 * val viewModel by sharedViewModel<SampleViewModel> { sampleFactory }
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[factory] Lambda expression to lazily get the factory to instantiate the view model.
 */
inline fun <reified T : ViewModel> Fragment.sharedViewModel(noinline factory: () -> ViewModelProvider.Factory) = SharedFactoryViewModel(T::class.java, factory)
