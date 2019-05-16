package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlin.reflect.KClass

/**
 * Delegate to lazily create an [EiffelViewModel].
 *
 * @param[V] Type of [EiffelViewModel] to create.
 * @param[S] Type of [State] associated with the view model.
 * @param[A] Type of [Action] supported by the view model.
 * @param[viewModelClass] Class representation of [EiffelViewModel] to create.
 * @param[owner] Lambda expression returning the [ViewModelStoreOwner] the view model should be scoped to.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to create the view model.
 */
class EiffelViewModelLazy<V : EiffelViewModel<S, A>, S : State, A : Action>(
    private val viewModelClass: KClass<V>,
    private val owner: () -> ViewModelStoreOwner,
    private val factory: () -> EiffelFactory<V, S, A>
) : Lazy<V> {

    private var cached: V? = null

    override val value: V
        get() = cached ?: ViewModelProvider(owner(), factory()).get(viewModelClass.java).also { cached = it }

    override fun isInitialized() = cached != null
}
