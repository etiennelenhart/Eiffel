package com.etiennelenhart.eiffel.factory

import com.etiennelenhart.eiffel.view.EiffelActivity
import com.etiennelenhart.eiffel.view.EiffelArguments
import com.etiennelenhart.eiffel.view.EiffelFragment

/**
 * Base class for an [EiffelFactory] which provides [EiffelArguments] that may be injected when creating the view model.
 *
 * @param[A] Type of [EiffelArguments] to provide.
 */
abstract class EiffelArgumentFactory<A : EiffelArguments> : EiffelFactory {

    /**
     * Provided [EiffelArguments]. Automatically set when used in corresponding view model delegates of [EiffelActivity] and [EiffelFragment].
     */
    var arguments: A? = null
}
