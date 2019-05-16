package com.etiennelenhart.eiffel.viewmodel.delegate

import androidx.appcompat.app.AppCompatActivity
import androidx.savedstate.SavedStateRegistryOwner
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.factory.DefaultEiffelFactory
import com.etiennelenhart.eiffel.factory.EiffelFactory
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Delegate to lazily create an [EiffelViewModel] scoped to this Activity. Passes provided [arguments] to given [factory].
 *
 * @param[V] Type of [EiffelViewModel] to create.
 * @param[S] Type of [State] associated to the view model.
 * @param[A] Type of [Action] supported by the view model.
 * @param[arguments] Lambda expression returning the arguments to pass, defaults to getting extra put with [Eiffel.KEY_ARGS].
 * @param[factory] Lambda expression returning the [EiffelFactory] used to create the view model, defaults to [DefaultEiffelFactory].
 */
inline fun <reified V : EiffelViewModel<S, A>, reified S : State, A : Action> AppCompatActivity.eiffelViewModel(
    noinline arguments: () -> Any? = { intent.extras?.get(Eiffel.KEY_ARGS) },
    noinline factory: (owner: SavedStateRegistryOwner) -> EiffelFactory<V, S, A> = { DefaultEiffelFactory(it) }
) = EiffelViewModelLazy(
    viewModelClass = V::class,
    owner = { this },
    factory = {
        factory(this).also {
            if (it is DefaultEiffelFactory) it.viewModelClass = V::class.java
            it.stateClass = S::class.java
            it.arguments = arguments()
        }
    }
)
