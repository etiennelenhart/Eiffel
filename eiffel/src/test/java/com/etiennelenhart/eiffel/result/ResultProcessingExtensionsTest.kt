package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ResultProcessingExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'then' called THEN 'command' is invoked`() {
        val result = Result.Success("command")

        var actual = ""
        result.then {
            actual = "$it invoked"
            Result.Success(Unit)
        }

        assertEquals("command invoked", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'then' called THEN 'command' is not invoked`() {
        val result = Result.Error()

        var actual = ""
        result.then {
            actual = "command invoked"
            Result.Success(Unit)
        }

        assertNotEquals("command invoked", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'then' called THEN error is forwarded`() {
        val result = Result.Error()

        val actual = result.then { Result.Success(0) }

        assertEquals(result, actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'map' called THEN 'transform' is applied`() {
        val result = Result.Success(0)

        val actual = (result.map { it.inc() } as Result.Success).data

        assertEquals(1, actual)
    }

    private object TestError : ErrorType
    private object TestError2 : ErrorType

    @Test
    fun `GIVEN Result Error WHEN 'mapError' called THEN 'transform' is applied`() {
        val result = Result.Error(TestError)

        val actual = (result.mapError { if (it === TestError) TestError2 else TestError } as Result.Error).type

        assertEquals(TestError2, actual)
    }
}
