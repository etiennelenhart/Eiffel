package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("IllegalIdentifier")
class ResultTest {

    @Test
    fun `GIVEN Result Success WHEN 'status' queried THEN 'success' is returned`() {
        val resource = Result.Success

        val actual = resource.status

        assertEquals(Status.SUCCESS, actual)
    }

    @Test
    fun `GIVEN Result Pending WHEN 'status' queried THEN 'pending' is returned`() {
        val resource = Result.Pending

        val actual = resource.status

        assertEquals(Status.PENDING, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'status' queried THEN 'error' is returned`() {
        val resource = Result.Error()

        val actual = resource.status

        assertEquals(Status.ERROR, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'type' queried THEN 'unspecified' is returned`() {
        val resource = Result.Error()

        val actual = resource.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN Result Error with 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val resource = Result.Error(type)

        val actual = resource.type

        assertEquals(type, actual)
    }
}
