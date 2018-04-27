package com.etiennelenhart.eiffel.livedata

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceTest {

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Pending(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Success(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Failure(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' WHEN 'type' queried THEN 'unspecified' is returned`() {
        val resource = Resource.Failure("")

        val actual = resource.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' and 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val resource = Resource.Failure("", type)

        val actual = resource.type

        assertEquals(type, actual)
    }
}
