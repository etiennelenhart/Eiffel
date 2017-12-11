package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import com.etiennelenhart.eiffel.Status
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("IllegalIdentifier")
class ResultWithDataTest {

    @Test
    fun `GIVEN ResultWithData Success with 'data' WHEN 'status' queried THEN 'success' is returned`() {
        val resource = ResultWithData.Success("")

        val actual = resource.status

        assertEquals(Status.SUCCESS, actual)
    }

    @Test
    fun `GIVEN ResultWithData Success with 'data' WHEN 'data' queried THEN 'data' is returned`() {
        val data = "data"
        val resource = ResultWithData.Success(data)

        val actual = resource.data

        assertEquals(data, actual)
    }

    @Test
    fun `GIVEN ResultWithData Pending with 'data' WHEN 'status' queried THEN 'pending' is returned`() {
        val resource = ResultWithData.Pending("")

        val actual = resource.status

        assertEquals(Status.PENDING, actual)
    }

    @Test
    fun `GIVEN ResultWithData Pending with 'data' WHEN 'data' queried THEN 'data' is returned`() {
        val data = "data"
        val resource = ResultWithData.Pending(data)

        val actual = resource.data

        assertEquals(data, actual)
    }

    @Test
    fun `GIVEN ResultWithData Error with 'default' WHEN 'status' queried THEN 'error' is returned`() {
        val resource = ResultWithData.Error("")

        val actual = resource.status

        assertEquals(Status.ERROR, actual)
    }

    @Test
    fun `GIVEN ResultWithData Error with 'default' WHEN 'data' queried THEN 'default' is returned`() {
        val default = "default"
        val resource = ResultWithData.Error(default)

        val actual = resource.data

        assertEquals(default, actual)
    }

    @Test
    fun `GIVEN ResultWithData Error with 'default' WHEN 'type' queried THEN 'unspecified' is returned`() {
        val resource = ResultWithData.Error("")

        val actual = resource.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN ResultWithData Error with 'default' and 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val resource = ResultWithData.Error("", type)

        val actual = resource.type

        assertEquals(type, actual)
    }
}
