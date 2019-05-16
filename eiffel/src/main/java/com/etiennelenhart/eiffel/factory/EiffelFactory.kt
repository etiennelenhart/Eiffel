package com.etiennelenhart.eiffel.factory

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateVMFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.state.Action
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Factory responsible for creating instances of [EiffelViewModel].
 *
 * @param[V] Type of [EiffelViewModel] to create.
 * @param[S] Type of [State] associated with the view model.
 * @param[A] Type of [Action] supported by the view model.
 * @param[owner] [SavedStateRegistryOwner] providing restored state for created [EiffelViewModel] instances.
 */
abstract class EiffelFactory<V : EiffelViewModel<S, A>, S : State, A : Action>(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateVMFactory(owner, null) {

    /**
     * Used internally to create an instance of [S].
     */
    lateinit var stateClass: Class<S>

    /**
     * Contains any arguments set with [Eiffel.KEY_ARGS] on the given owner.
     */
    var arguments: Any? = null

    /**
     * Creates an instance of the view model [V], [initialState] is provided. If state [S] has a secondary constructor matching the [arguments] set on the
     * view model's corresponding owner, it will be called automatically. Any state restoration is performed before calling this function so [initialState]
     * is already fully restored.
     *
     * @param[initialState] Initial state of [S].
     * @return Instance of view model [V].
     */
    protected abstract fun create(initialState: S): V

    @Suppress("UNCHECKED_CAST")
    final override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
        val state = try {
            initialState()
        } catch (e: Exception) {
            throw IllegalStateException(
                """
                Creating initial state of ${stateClass.simpleName} failed. Make sure that one of the following conditions is met:
                 1. State class has default values for all constructor parameters.
                 2. State class has secondary constructor for ${arguments?.javaClass?.simpleName ?: "Activity or Fragment arguments"}.
                 3. Factory class responsible for creating the view model overrides 'initialState()'.
            """.trimIndent()
            )
        }
        val restoredState = handle.get<Bundle>(SAVED_EIFFEL_STATE)?.let { state.restore(it) } ?: state
        return create(restoredState as S).apply { saveState = { handle.set(SAVED_EIFFEL_STATE, it) } } as T
    }

    /**
     * Override this if the initial state of [S] requires information from external dependencies.
     *
     * *Note: Any secondary constructor matching [arguments] won't be called automatically then.*
     *
     * @return Initial state of [S].
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun initialState(): S = arguments?.let { args ->
        val argumentsConstructor = stateClass.constructors.firstOrNull {
            it.parameterTypes.size == 1 && it.parameterTypes[0].isAssignableFrom(args::class.java)
        }
        argumentsConstructor?.newInstance(args) as? S
    } ?: stateClass.newInstance()

    internal companion object {
        const val SAVED_EIFFEL_STATE = "saved_eiffel_state"
    }
}
