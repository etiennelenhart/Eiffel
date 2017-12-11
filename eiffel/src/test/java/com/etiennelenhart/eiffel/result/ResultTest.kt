package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("IllegalIdentifier")
class ResultTest {

    @Test
    fun `GIVEN Result Success WHEN 'status' queried THEN 'success' is returned`() {
        val result = Result.Success

        val actual = result.status

        assertEquals(Status.SUCCESS, actual)
    }

    @Test
    fun `GIVEN Result Pending WHEN 'status' queried THEN 'pending' is returned`() {
        val result = Result.Pending

        val actual = result.status

        assertEquals(Status.PENDING, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'status' queried THEN 'error' is returned`() {
        val result = Result.Error()

        val actual = result.status

        assertEquals(Status.ERROR, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'type' queried THEN 'unspecified' is returned`() {
        val result = Result.Error()

        val actual = result.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN Result Error with 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val result = Result.Error(type)

        val actual = result.type

        assertEquals(type, actual)
    }
}
