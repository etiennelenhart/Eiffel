package com.etiennelenhart.eiffel.state

import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Marker interface for data classes that represent state.
 *
 * Only use `val`s and immutable data structures for properties and try
 * to avoid view specifics like resource IDs or data binding logic.
 * Implementing classes can be used as state in [EiffelViewModel].
 *
 * You may want to override the [toString] if you are using [Eiffel.debugMode] as the logger
 * will convert the state to a string.  And if your [State] contains a large list of complex
 * items, it may pollute the logcat.
 */
interface State
