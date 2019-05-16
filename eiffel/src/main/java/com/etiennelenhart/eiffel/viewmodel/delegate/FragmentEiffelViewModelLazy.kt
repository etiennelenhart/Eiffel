package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.fragment.app.Fragment
import androidx.savedstate.SavedStateRegistryOwner
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.factory.DefaultEiffelFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Delegate to lazily create an [EiffelViewModel] scoped to the given [owner]. Passes provided [arguments] to given [factory].
 *
 * @param[V] Type of [EiffelViewModel] to create.
 * @param[S] Type of [State] associated with the view model.
 * @param[A] Type of [Action] supported by the view model.
 * @param[owner] Lambda expression returning the [Fragment] the view model should be scoped to, defaults to this Fragment.
 * @param[arguments] Lambda expression returning the arguments to pass, defaults to getting arguments put with [Eiffel.KEY_ARGS] on the given [owner].
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to [DefaultEiffelFactory].
 */
inline fun <reified V : EiffelViewModel<S, A>, reified S : State, A : Action> Fragment.eiffelViewModel(
    noinline owner: () -> Fragment = { this },
    noinline arguments: () -> Any? = { owner().arguments?.get(Eiffel.KEY_ARGS) },
    noinline factory: (owner: SavedStateRegistryOwner) -> EiffelFactory<V, S, A> = { DefaultEiffelFactory(it) }
) = EiffelViewModelLazy(
    viewModelClass = V::class,
    owner = owner,
    factory = {
        factory(owner()).also {
            if (it is DefaultEiffelFactory) it.viewModelClass = V::class.java
            it.stateClass = S::class.java
            it.arguments = arguments()
        }
    }
)

/**
 * Delegate to lazily create an [EiffelViewModel] scoped to the Activity associated with this Fragment. Passes Activity's extra to given [factory], if one
 * was put with [Eiffel.KEY_ARGS].
 *
 * @param[V] Type of [EiffelViewModel] to create.
 * @param[S] Type of [State] associated with the view model.
 * @param[A] Type of [Action] supported by the view model.
 * @param[factory] Lambda expression returning the [EiffelFactory] used to instantiate the view model, defaults to [DefaultEiffelFactory].
 */
inline fun <reified V : EiffelViewModel<S, A>, reified S : State, A : Action> Fragment.activityEiffelViewModel(
    noinline factory: (owner: SavedStateRegistryOwner) -> EiffelFactory<V, S, A> = { DefaultEiffelFactory(it) }
): EiffelViewModelLazy<V, S, A> {
    return EiffelViewModelLazy(
        viewModelClass = V::class,
        owner = { requireActivity() },
        factory = {
            factory(requireActivity()).also {
                if (it is DefaultEiffelFactory) it.viewModelClass = V::class.java
                it.stateClass = S::class.java
                it.arguments = requireActivity().intent.extras?.get(Eiffel.KEY_ARGS)
            }
        }
    )
}
