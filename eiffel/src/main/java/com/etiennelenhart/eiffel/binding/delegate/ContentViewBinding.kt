package com.etiennelenhart.eiffel.binding.delegate

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to lazily provide a [ViewDataBinding] from [DataBindingUtil.setContentView].
 *
 * May be used in an Activity like this:
 * ```
 * val binding by ContentViewBinding<ActivitySampleBinding>(R.layout.activity_sample)
 * ```
 *
 * @param[T] Type of the provided binding.
 * @param[layoutId] ID of the layout to bind and set as content view.
 */
class ContentViewBinding<out T : ViewDataBinding>(@LayoutRes private val layoutId: Int) : ReadOnlyProperty<Activity, T> {

    private var value: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        if (value == null) value = DataBindingUtil.setContentView(thisRef, layoutId)
        return value!!
    }
}
