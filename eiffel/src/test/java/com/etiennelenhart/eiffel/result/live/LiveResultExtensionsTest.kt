package com.etiennelenhart.eiffel.result.live

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class LiveResultExtensionsTest {

    @Test
    fun `GIVEN LiveResult Pending with 'data' WHEN 'on' called THEN 'onPending' is invoked`() {
        val result = LiveResult.Pending("block")

        val actual = result.on({ it }, { "" }, { "" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Success with 'data' WHEN 'on' called THEN 'onSuccess' is invoked`() {
        val result = LiveResult.Success("block")

        val actual = result.on({ "" }, { it }, { "" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Failure WHEN 'on' called THEN 'onFailure' is invoked`() {
        val result = LiveResult.Failure()

        val actual = result.on({ "" }, { "" }, { "block" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Pending with 'data' WHEN 'isPending' called THEN 'block' is invoked`() {
        val result = LiveResult.Pending("block")

        var actual = ""
        result.isPending { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Success with 'data' WHEN 'isPending' called THEN 'block' is not invoked`() {
        val result = LiveResult.Success("block")

        var actual = ""
        result.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Failure WHEN 'isPending' called THEN 'block' is not invoked`() {
        val result = LiveResult.Failure()

        var actual = ""
        result.isPending { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Success with 'data' WHEN 'isSuccess' called THEN 'block' is invoked`() {
        val result = LiveResult.Success("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Pending with 'data' WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val result = LiveResult.Pending("block")

        var actual = ""
        result.isSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Failure WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val result = LiveResult.Failure()

        var actual = ""
        result.isSuccess { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Failure WHEN 'isError' called THEN 'block' is invoked`() {
        val result = LiveResult.Failure()

        var actual = ""
        result.isFailure { actual = "block" }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Success WHEN 'isError' called THEN 'block' is not invoked`() {
        val result = LiveResult.Success(Unit)

        var actual = ""
        result.isFailure { actual = "block" }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN LiveResult Pending WHEN 'isError' called THEN 'block' is not invoked`() {
        val result = LiveResult.Pending(Unit)

        var actual = ""
        result.isFailure { actual = "block" }

        assertNotEquals("block", actual)
    }
}
