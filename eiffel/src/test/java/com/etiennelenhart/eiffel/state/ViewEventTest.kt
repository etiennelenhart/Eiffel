package com.etiennelenhart.eiffel.state

import org.junit.Assert.assertTrue
import org.junit.Test

@Suppress("IllegalIdentifier")
class ViewEventTest {

    @Test
    fun `GIVEN View Event None WHEN 'handled' queried THEN 'true' is returned`() {
        val event = ViewEvent.None

        val actual = event.handled

        assertTrue(actual)
    }
}
