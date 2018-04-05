package com.etiennelenhart.eiffel.result.live

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Test
import kotlin.test.assertEquals

class LiveResultProcessingExtensionsTest {

    @Test
    fun `GIVEN LiveResult Pending with 'data' WHEN 'mapPending' called THEN 'transform' is applied`() {
        val result = LiveResult.Pending(0)

        val actual = (result.mapPending { it.inc() } as LiveResult.Pending).data

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN LiveResult Success with 'data' WHEN 'map' called THEN 'transform' is applied`() {
        val result = LiveResult.Success(0)

        val actual = (result.map { it.inc() } as LiveResult.Success).data

        assertEquals(1, actual)
    }

    private object TestError : ErrorType
    private object TestError2 : ErrorType

    @Test
    fun `GIVEN LiveResult Failure with 'type' WHEN 'mapFailure' called THEN 'transform' is applied`() {
        val result = LiveResult.Failure(TestError)

        val actual = (result.mapFailure { if (it === TestError) TestError2 else TestError } as LiveResult.Failure).type

        assertEquals(TestError2, actual)
    }
}
