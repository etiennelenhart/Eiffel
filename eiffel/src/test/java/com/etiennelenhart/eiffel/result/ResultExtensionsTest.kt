package com.etiennelenhart.eiffel.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ResultExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'isSuccess' called THEN 'block' is invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'isPending' called THEN 'block' is invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.isPending { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'isPending' called THEN 'block' is not invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'isPending' called THEN 'block' is not invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'isError' called THEN 'block' is invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.isError { data, _ -> actual = data }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'isError' called THEN 'block' is not invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.isError { data, _ -> actual = data }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'isError' called THEN 'block' is not invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.isError { data, _ -> actual = data }

        assertNotEquals("block", actual)
    }
}
