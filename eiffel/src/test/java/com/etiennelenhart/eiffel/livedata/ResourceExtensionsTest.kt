package com.etiennelenhart.eiffel.livedata

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ResourceExtensionsTest {

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'fold' called THEN 'onPending' is invoked`() {
        val resource = Resource.Pending("block")

        val actual = resource.fold({ it }, { "" }, { _, _ -> "" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'fold' called THEN 'onSuccess' is invoked`() {
        val resource = Resource.Success("block")

        val actual = resource.fold({ "" }, { it }, { _, _ -> "" })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Failure with 'value' WHEN 'fold' called THEN 'onFailure' is invoked`() {
        val resource = Resource.Failure("block")

        val actual = resource.fold({ "" }, { "" }, { value, _ -> value })

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'isSuccess' called THEN 'block' is invoked`() {
        val resource = Resource.Success("block")

        var actual = ""
        resource.isSuccess { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val resource = Resource.Pending("block")

        var actual = ""
        resource.isSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Failure WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val resource = Resource.Failure("block")

        var actual = ""
        resource.isSuccess { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'isPending' called THEN 'block' is invoked`() {
        val resource = Resource.Pending("block")

        var actual = ""
        resource.isPending { actual = it }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'isPending' called THEN 'block' is not invoked`() {
        val resource = Resource.Success("block")

        var actual = ""
        resource.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Failure WHEN 'isPending' called THEN 'block' is not invoked`() {
        val resource = Resource.Failure("block")

        var actual = ""
        resource.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Failure WHEN 'isFailure' called THEN 'block' is invoked`() {
        val resource = Resource.Failure("block")

        var actual = ""
        resource.isFailure { value, _ -> actual = value }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'isFailure' called THEN 'block' is not invoked`() {
        val resource = Resource.Success("block")

        var actual = ""
        resource.isFailure { value, _ -> actual = value }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'isFailure' called THEN 'block' is not invoked`() {
        val resource = Resource.Pending("block")

        var actual = ""
        resource.isFailure { value, _ -> actual = value }

        assertNotEquals("block", actual)
    }
}
