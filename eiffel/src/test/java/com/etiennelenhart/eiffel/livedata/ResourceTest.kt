package com.etiennelenhart.eiffel.livedata

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("IllegalIdentifier")
class ResourceTest {

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'status' queried THEN 'success' is returned`() {
        val resource = Resource.Success("")

        val actual = resource.status

        assertEquals(Status.SUCCESS, actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Success(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'status' queried THEN 'pending' is returned`() {
        val resource = Resource.Pending("")

        val actual = resource.status

        assertEquals(Status.PENDING, actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Pending(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' WHEN 'status' queried THEN 'error' is returned`() {
        val resource = Resource.Error("")

        val actual = resource.status

        assertEquals(Status.ERROR, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Error(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' WHEN 'type' queried THEN 'unspecified' is returned`() {
        val resource = Resource.Error("")

        val actual = resource.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN Resource Error with 'value' and 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val resource = Resource.Error("", type)

        val actual = resource.type

        assertEquals(type, actual)
    }
}
