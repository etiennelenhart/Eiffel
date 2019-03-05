package com.etiennelenhart.eiffel.view.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.view.EiffelFragment
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Delegate to lazily initialize an [EiffelViewModel].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[owner] Lambda expression returning the [ViewModelStoreOwner] the view model should be scoped to, defaults to this [EiffelFragment].
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to calling empty constructor of [V].
 */
inline fun <reified V : ViewModel> EiffelFragment.eiffelViewModel(
    noinline owner: () -> ViewModelStoreOwner = { this },
    noinline factory: () -> EiffelFactory = { EiffelFactory.Default }
) = EiffelViewModelLazy(V::class, owner, factory)

/**
 * Delegate to lazily initialize an [EiffelViewModel] scoped to this [EiffelFragment]. Passes provided [arguments] to given [factory].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[A] Type of arguments to pass to the [factory].
 * @param[arguments] Lambda expression returning the arguments to pass, defaults to calling [EiffelFragment.getEiffelArguments].
 * @param[factory] Lambda expression returning the [EiffelArgumentFactory] used to instantiate the view model.
 */
inline fun <reified V : ViewModel, reified A : Any> EiffelFragment.argsEiffelViewModel(
    noinline arguments: () -> A = {
        getEiffelArguments() as? A
            ?: throw IllegalArgumentException("${this::class.java.simpleName} doesn't provide expected ${A::class.java.simpleName} arguments")
    },
    noinline factory: () -> EiffelArgumentFactory<A>
): EiffelViewModelLazy<V> {
    return EiffelViewModelLazy(V::class, { this }, { factory().apply { this.arguments = arguments() } })
}

/**
 * Delegate to lazily initialize an [EiffelViewModel] scoped to the Activity associated with this [EiffelFragment].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to calling empty constructor of [V].
 */
inline fun <reified V : ViewModel> EiffelFragment.activityEiffelViewModel(
    noinline factory: () -> EiffelFactory = { EiffelFactory.Default }
) = EiffelViewModelLazy(V::class, ::requireActivity, factory)
