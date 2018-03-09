package com.etiennelenhart.eiffel.livedata

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ResourceExtensionsTest {

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
    fun `GIVEN Resource Error WHEN 'isSuccess' called THEN 'block' is not invoked`() {
        val resource = Resource.Error("block")

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
    fun `GIVEN Resource Error WHEN 'isPending' called THEN 'block' is not invoked`() {
        val resource = Resource.Error("block")

        var actual = ""
        resource.isPending { actual = it }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Error WHEN 'isError' called THEN 'block' is invoked`() {
        val resource = Resource.Error("block")

        var actual = ""
        resource.isError { value, _ -> actual = value }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Success with 'value' WHEN 'isError' called THEN 'block' is not invoked`() {
        val resource = Resource.Success("block")

        var actual = ""
        resource.isError { value, _ -> actual = value }

        assertNotEquals("block", actual)
    }

    @Test
    fun `GIVEN Resource Pending with 'value' WHEN 'isError' called THEN 'block' is not invoked`() {
        val resource = Resource.Pending("block")

        var actual = ""
        resource.isError { value, _ -> actual = value }

        assertNotEquals("block", actual)
    }
}
