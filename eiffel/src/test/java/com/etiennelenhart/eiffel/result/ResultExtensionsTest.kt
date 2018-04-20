package com.etiennelenhart.eiffel.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ResultExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'fold' called THEN 'onSuccess' is invoked`() {
        val result = Result.Success("block")

        val actual = result.fold({ it }, { "" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'fold' called THEN 'onError' is invoked`() {
        val result = Result.Error()

        val actual = result.fold({ it }, { "block" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'isSuccess' called THEN 'block' is invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val result = Result.Error()

        var actual = ""
        result.isSuccess { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'isError' called THEN 'block' is invoked`() {
        val result = Result.Error()

        var actual = ""
        result.isError { actual = "block" }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success WHEN 'isError' called THEN 'block' is not invoked`() {
        val result = Result.Success(Unit)

        var actual = ""
        result.isError { actual = "block" }

        assertNotEquals("block", actual)
    }
}
