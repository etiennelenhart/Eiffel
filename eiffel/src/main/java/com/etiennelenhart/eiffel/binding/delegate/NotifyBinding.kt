package com.etiennelenhart.eiffel.binding.delegate

import androidx.databinding.BaseObservable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Property delegate to notify a property change to a data binding.
 *
 * The delegating property should be annotated with `@get:Bindable` to generate a field in BR.
 * May be used in a [BaseObservable] like this:
 * ```
 * @get:Bindable
 * var sampleValue by NotifyBinding("", BR.sampleValue)
 * ```
 *
 * @param[T] Type of the notifying property.
 * @param[value] Initial value.
 * @param[fieldId] The id of the generated BR field.
 */
class NotifyBinding<T : Any>(private var value: T, private val fieldId: Int) : ReadWriteProperty<BaseObservable, T> {

    override fun getValue(thisRef: BaseObservable, property: KProperty<*>) = value

    override fun setValue(thisRef: BaseObservable, property: KProperty<*>, value: T) {
        this.value = value
        thisRef.notifyPropertyChanged(fieldId)
    }
}

/**
 * Convenience extension function for the [NotifyBinding] delegate.
 *
 * The delegating property should be annotated with `@get:Bindable` to generate a field in BR.
 * May be used in a [BaseObservable] like this:
 * ```
 * @get:Bindable
 * var sampleValue by notifyBinding("", BR.sampleValue)
 * ```
 *
 * @param[T] Type of the notifying property.
 * @param[value] Initial value.
 * @param[fieldId] The id of the generated BR field.
 */
fun <T : Any> BaseObservable.notifyBinding(value: T, fieldId: Int) = NotifyBinding(value, fieldId)
