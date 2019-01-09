package com.etiennelenhart.eiffel.state

import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Marker interface for data classes that represent state.
 *
 * Only use `val`s and immutable data structures for properties and try
 * to avoid view specifics like resource IDs or data binding logic.
 * Implementing classes can be used as state in [EiffelViewModel].
 */
interface State
