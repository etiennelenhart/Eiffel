package com.etiennelenhart.eiffel.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@Suppress("IllegalIdentifier")
class ResultExtensionsTest {

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'on' called THEN 'succeeded' is invoked`() {
        val result = Result.Success("succeeded")

        val actual = result.on(
            succeeded = { it },
            isPending = { "pending" },
            failed = { "failed" }
        )

        assertEquals("succeeded", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'on' called THEN 'isPending' is invoked`() {
        val result = Result.Pending("pending")

        val actual = result.on(
            succeeded = { "succeeded" },
            isPending = { it },
            failed = { "failed" }
        )

        assertEquals("pending", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'on' called THEN 'failed' is invoked`() {
        val result = Result.Error()

        val actual = result.on(
            succeeded = { "succeeded" },
            isPending = { "pending" },
            failed = { "failed" }
        )

        assertEquals("failed", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'succeeded' called THEN 'block' is invoked`() {
        val result = Result.Success("block")

        var actual = ""
        result.succeeded { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'succeeded' called THEN 'block' is not invoked`() {
        val result = Result.Pending("block")

        var actual = ""
        result.succeeded { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'succeeded' called THEN 'block' is not invoked`() {
        val result = Result.Error()

        var actual = ""
        result.succeeded { actual = "block" }

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
        val result = Result.Error()

        var actual = ""
        result.isPending { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Error WHEN 'failed' called THEN 'block' is invoked`() {
        val result = Result.Error()

        var actual = ""
        result.failed { actual = "block" }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Success with 'data' WHEN 'failed' called THEN 'block' is not invoked`() {
        val result = Result.Success(0)

        var actual = ""
        result.failed { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Result Pending with 'data' WHEN 'failed' called THEN 'block' is not invoked`() {
        val result = Result.Pending(0)

        var actual = ""
        result.failed { actual = "block" }

        assertNotEquals("block", actual)
    }
}
