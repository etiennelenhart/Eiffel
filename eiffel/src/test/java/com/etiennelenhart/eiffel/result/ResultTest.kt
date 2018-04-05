package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class ResultTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'data' queried THEN 'data' is returned`() {
        val data = "data"
        val result = Result.Success(data)

        val actual = result.data

        assertEquals(data, actual)
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
