package com.etiennelenhart.eiffel.livedata

import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceTest {

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Success(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Pending(value)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Failure WHEN 'value' queried THEN 'value' is returned`() {
        val value = "value"
        val resource = Resource.Failure(value, 0)

        val actual = resource.value

        assertEquals(value, actual)
    }

    @Test
    fun `GIVEN Resource Failure WHEN 'error' queried THEN 'error' is returned`() {
        val resource = Resource.Failure("", 0)

        val actual = resource.error

        assertEquals(0, actual)
    }
}
