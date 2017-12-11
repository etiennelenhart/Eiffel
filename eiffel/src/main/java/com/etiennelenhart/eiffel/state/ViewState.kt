package com.etiennelenhart.eiffel.state

/**
 * Marker interface for data classes that represent a view state.
 *
 * Only use `val`s and immutable data structures for properties and try
 * to avoid view specifics like resource IDs or data binding logic.
 * Implementing classes can be used as state in [StateViewModel].
 */
interface ViewState
