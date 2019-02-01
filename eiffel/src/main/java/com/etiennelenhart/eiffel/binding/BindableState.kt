package com.etiennelenhart.eiffel.binding

/**
 * Marker interface for data classes that represent state that can be used for data binding.
 *
 * Only use `val`s and immutable data structures for properties. May contain view specifics like resource IDs and layout logic.
 */
interface BindableState
