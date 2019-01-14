package com.etiennelenhart.eiffel.state

import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Marker interface for actions that can be dispatched to [EiffelViewModel] in order to update state.
 *
 * Implementing classes are best declared as 'sealed':
 *
 * ```
 * sealed class SampleAction : Action {
 *     class DoSample(val index: Int) : SampleAction()
 *     object DoingSample : SampleAction()
 *     class UpdateSample(val sample: String) : SampleAction()
 * }
 * ```
 */
interface Action
