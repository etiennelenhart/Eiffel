package com.etiennelenhart.eiffel.result

import com.etiennelenhart.eiffel.ErrorType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

@Suppress("IllegalIdentifier")
class ResultProcessingExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'then' called THEN 'command' is invoked`() {
        val result = Result.Success("command")

        val actual = result.then { Result.Success("$it invoked") }.data

        assertEquals("command invoked", actual)
    }

    @Test
    fun `GIVEN Result Pending WHEN 'then' called THEN 'IllegalStateException' is thrown`() {
        val result = Result.Pending(0)

        val actual: () -> Unit = { result.then { Result.Success(0) } }

        assertFailsWith(IllegalStateException::class, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'then' called THEN 'command' is not invoked`() {
        val result = Result.Error()

        val actual = result.then { Result.Success("command invoked") }.data

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

        val actual = result.map { it.inc() }.data

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'map' called THEN 'transform' is applied to 'data'`() {
        val result = Result.Pending(0)

        val actual = result.map { it.inc() }.data

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'map' called THEN error is forwarded`() {
        val result = Result.Error()

        val actual = result.map {}

        assertEquals(result, actual)
    }

    @Test
    fun `GIVEN Result Success WHEN 'mapError' called THEN success is forwarded`() {
        val result = Result.Success(0)

        val actual = result.mapError { ErrorType.Unspecified }

        assertEquals(result, actual)
    }

    @Test
    fun `GIVEN Result Pending WHEN 'mapError' called THEN pending is forwarded`() {
        val result = Result.Pending(0)

        val actual = result.mapError { ErrorType.Unspecified }

        assertEquals(result, actual)
    }

    private object TestError : ErrorType

    @Test
    fun `GIVEN Result Error WHEN called THEN 'transform' is applied to 'type'`() {
        val result = Result.Error()

        val actual = result.mapError { TestError }

        assertEquals(TestError, (actual as Result.Error).type)
    }
}
