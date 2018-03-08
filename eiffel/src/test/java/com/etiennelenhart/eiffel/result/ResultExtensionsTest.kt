package com.etiennelenhart.eiffel.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Suppress("IllegalIdentifier")
class ResultExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'on' called THEN 'success' is invoked`() {
        val result = Result.Success("success")

        val actual = result.on(
            success = { it },
            isPending = { "pending" },
            error = { _, _ -> "error" }
        )

        assertEquals("success", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'on' called THEN 'isPending' is invoked`() {
        val result = Result.Pending("pending")

        val actual = result.on(
            success = { "success" },
            isPending = { it },
            error = { _, _ -> "error" }
        )

        assertEquals("pending", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'on' called THEN 'error' is invoked`() {
        val result = Result.Error("error")

        val actual = result.on(
            success = { "success" },
            isPending = { "pending" },
            error = { data, _ -> data }
        )

        assertEquals("error", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'onSuccess' called THEN 'block' is invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.onSuccess { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'onSuccess' called THEN 'block' is not invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.onSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'onSuccess' called THEN 'block' is not invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.onSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'onPending' called THEN 'block' is invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.onPending { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'onPending' called THEN 'block' is not invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.onPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'onPending' called THEN 'block' is not invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.onPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'onError' called THEN 'block' is invoked`() {
        val result = Result.Error("block")

        var actual = ""
        result.onError { data, _ -> actual = data }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'onError' called THEN 'block' is not invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.onError { data, _ -> actual = data }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'onError' called THEN 'block' is not invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.onError { data, _ -> actual = data }

        assertNotEquals("block", actual)
    }
}
