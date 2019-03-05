package com.etiennelenhart.eiffel.view.delegate

import androidx.lifecycle.ViewModel
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.view.EiffelActivity
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
 * Delegate to lazily initialize an [EiffelViewModel] scoped to this [EiffelActivity]. Passes provided [arguments] to given [factory].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[A] Type of arguments to pass to the [factory].
 * @param[arguments] Lambda expression returning the arguments to pass, defaults to calling [EiffelActivity.getEiffelExtra].
 * @param[factory] Lambda expression returning the [EiffelArgumentFactory] used to instantiate the view model.
 */
inline fun <reified V : ViewModel, reified A : Any> EiffelActivity.argsEiffelViewModel(
    noinline arguments: () -> A = {
        getEiffelExtra() as? A ?: throw IllegalArgumentException("${this::class.java.simpleName} doesn't provide expected ${A::class.java.simpleName} extra")
    },
    noinline factory: () -> EiffelArgumentFactory<A>
) = EiffelViewModelLazy(V::class, { this }, { factory().apply { this.arguments = arguments() } })
