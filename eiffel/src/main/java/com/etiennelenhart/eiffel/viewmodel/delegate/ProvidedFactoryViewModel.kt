package com.etiennelenhart.eiffel.viewmodel.delegate

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a [ViewModel] instantiated by a [ViewModelProvider.Factory]
 * from this Activity's [ViewModelProvider].
 *
 * May be used in a [FragmentActivity] like this:
 * ```
 * val viewModel by ProvidedFactoryViewModel(SampleViewModel::class.java, sampleFactory)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[viewModelClass] Java class of the provided view model.
 * @param[factory] Block to lazily get the factory to instantiate the view model.
 */
class ProvidedFactoryViewModel<out T : ViewModel>(private val viewModelClass: Class<T>,
                                                  private val factory: () -> ViewModelProvider.Factory) : ReadOnlyProperty<FragmentActivity, T> {

    private var value: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) value = ViewModelProviders.of(thisRef, factory()).get(viewModelClass)
        return value!!
    }
}

/**
 * Convenience extension function to use [ProvidedFactoryViewModel] delegate with type parameter instead of Java class.
 *
 * May be used in a [FragmentActivity] like this:
 * ```
 * val viewModel by providedFactoryViewModel<SampleViewModel>(sampleFactory)
 * ```
 *
 * @param[T] Type of the provided view model.
 * @param[factory] Block to lazily get the factory to instantiate the view model.
 */
inline fun <reified T : ViewModel> FragmentActivity.providedFactoryViewModel(noinline factory: () -> ViewModelProvider.Factory): ProvidedFactoryViewModel<T> {
    return ProvidedFactoryViewModel(T::class.java, factory)
}
