package com.etiennelenhart.eiffel.factory

import androidx.savedstate.SavedStateRegistryOwner
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Default [EiffelFactory] used if no other is provided. Creates an instance of view model [V] and passes initial state of [S] to constructor.
 */
class DefaultEiffelFactory<V : EiffelViewModel<S, A>, S : State, A : Action>(
    owner: SavedStateRegistryOwner
) : EiffelFactory<V, S, A>(owner) {

    lateinit var viewModelClass: Class<V>

    @Suppress("UNCHECKED_CAST")
    override fun create(initialState: S): V {
        if (viewModelClass.constructors.size == 1) {
            val primaryConstructor = viewModelClass.constructors[0]
            if (primaryConstructor.parameterTypes.size == 1 && primaryConstructor.parameterTypes[0].isAssignableFrom(stateClass)) {
                return primaryConstructor.newInstance(initialState) as V
            }
        }
        throw IllegalStateException(
            "'${viewModelClass.simpleName}' should only have a primary constructor expecting a single '${stateClass.simpleName}' argument." +
                    " \nIf the view model requires additional dependencies create a subclass of 'EiffelFactory' and pass it to the respective" +
                    " 'eiffelViewModel()' delegate in an Activity or Fragment."
        )
    }
}
