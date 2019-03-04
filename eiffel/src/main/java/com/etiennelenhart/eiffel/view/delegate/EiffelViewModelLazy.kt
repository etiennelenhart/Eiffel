package com.etiennelenhart.eiffel.view.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlin.reflect.KClass

/**
 * Delegate to lazily initialize an [EiffelViewModel].
 *
 * @param[viewModelClass] Type of [EiffelViewModel] to initialize.
 * @param[owner] Lambda expression returning the [ViewModelStoreOwner] the view model should be scoped to.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model.
 */
class EiffelViewModelLazy<V : ViewModel>(
    private val viewModelClass: KClass<V>,
    private val owner: () -> ViewModelStoreOwner,
    private val factory: () -> EiffelFactory
) : Lazy<V> {

    private var cached: V? = null

    override val value: V
        get() = cached ?: ViewModelProvider(owner(), factory()).get(viewModelClass.java).also { cached = it }

    override fun isInitialized() = cached != null
}
