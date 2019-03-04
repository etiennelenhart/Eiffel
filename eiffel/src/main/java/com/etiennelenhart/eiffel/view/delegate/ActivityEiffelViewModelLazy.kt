package com.etiennelenhart.eiffel.view.delegate

import androidx.lifecycle.ViewModel
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.view.EiffelActivity
import com.etiennelenhart.eiffel.view.EiffelArguments
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Delegate to lazily initialize an [EiffelViewModel] scoped to this [EiffelActivity].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to calling empty constructor of [V].
 */
inline fun <reified V : ViewModel> EiffelActivity.eiffelViewModel(
    noinline factory: () -> EiffelFactory = { EiffelFactory.Default }
) = EiffelViewModelLazy(V::class, { this }, factory)

/**
 * Delegate to lazily initialize an [EiffelViewModel] scoped to this [EiffelActivity]. Passes provided [eiffelArguments] to given [factory].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[A] Type of [EiffelArguments] to pass to the [factory].
 * @param[eiffelArguments] Lambda expression returning the [EiffelArguments] that should be passed to the [factory].
 * @param[factory] Lambda expression returning the [EiffelArgumentFactory] used to instantiate the view model.
 */
inline fun <reified V : ViewModel, A : EiffelArguments> EiffelActivity.argsEiffelViewModel(
    noinline eiffelArguments: () -> A,
    noinline factory: () -> EiffelArgumentFactory<A>
) = EiffelViewModelLazy(V::class, { this }, { factory().apply { arguments = eiffelArguments() } })
