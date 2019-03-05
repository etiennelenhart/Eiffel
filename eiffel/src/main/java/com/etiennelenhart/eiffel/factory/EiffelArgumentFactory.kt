package com.etiennelenhart.eiffel.factory

import com.etiennelenhart.eiffel.view.EiffelActivity
import com.etiennelenhart.eiffel.view.EiffelFragment

/**
 * Base class for an [EiffelFactory] which provides arguments that may be injected when creating the view model.
 *
 * @param[A] Type of arguments to provide.
 */
abstract class EiffelArgumentFactory<A : Any> : EiffelFactory {

    /**
     * Provided arguments. Automatically set when used in corresponding view model delegates of [EiffelActivity] and [EiffelFragment].
     */
    lateinit var arguments: A
}
