package com.etiennelenhart.eiffel.result.live

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class LiveResultTest {

    @Test
    fun `GIVEN LiveResult Pending with 'data' WHEN 'data' queried THEN 'data' is returned`() {
        val data = "data"
        val result = LiveResult.Pending(data)

        val actual = result.data

        assertEquals(data, actual)
    }

    @Test
    fun `GIVEN LiveResult Success with 'data' WHEN 'data' queried THEN 'data' is returned`() {
        val data = "data"
        val result = LiveResult.Success(data)

        val actual = result.data

        assertEquals(data, actual)
    }

    @Test
    fun `GIVEN LiveResult Failure WHEN 'type' queried THEN 'unspecified' is returned`() {
        val result = LiveResult.Failure()

        val actual = result.type

        assertEquals(ErrorType.Unspecified, actual)
    }

    @Test
    fun `GIVEN LiveResult Failure with 'type' WHEN 'type' queried THEN 'type' is returned`() {
        val type = object : ErrorType {}
        val result = LiveResult.Failure(type)

        val actual = result.type

        assertEquals(type, actual)
    }
}
