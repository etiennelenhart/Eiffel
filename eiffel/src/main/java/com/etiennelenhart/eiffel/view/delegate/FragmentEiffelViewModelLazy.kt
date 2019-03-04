package com.etiennelenhart.eiffel.view.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.etiennelenhart.eiffel.factory.EiffelArgumentFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.view.EiffelArguments
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
 * Delegate to lazily initialize an [EiffelViewModel]. Gets arguments by calling [EiffelFragment.getEiffelArguments] and passes them to the given [factory].
 * Desired [EiffelArguments] should be set using [EiffelFragment.setEiffelArguments].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[A] Type of [EiffelArguments] to pass to the [factory].
 * @param[factory] Lambda expression returning the [EiffelArgumentFactory] used to instantiate the view model.
 */
inline fun <reified V : ViewModel, A : EiffelArguments> EiffelFragment.eiffelViewModel(
    noinline factory: () -> EiffelArgumentFactory<A>
) = EiffelViewModelLazy(V::class, { this }, { factory().apply { arguments = getEiffelArguments() } })

/**
 * Delegate to lazily initialize an [EiffelViewModel] scoped to the Activity associated with this [EiffelFragment].
 *
 * @param[V] Type of [EiffelViewModel] to initialize.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to calling empty constructor of [V].
 */
inline fun <reified V : ViewModel> EiffelFragment.activityEiffelViewModel(
    noinline factory: () -> EiffelFactory = { EiffelFactory.Default }
) = EiffelViewModelLazy(V::class, ::requireActivity, factory)
