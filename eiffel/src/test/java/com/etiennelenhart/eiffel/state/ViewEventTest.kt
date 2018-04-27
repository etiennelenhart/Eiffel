package com.etiennelenhart.eiffel.state

import org.junit.Test
import kotlin.test.assertEquals

class ViewEventTest {

    class TestEvent : ViewEvent()

    @Test
    fun `GIVEN ViewEvent WHEN 'handle' called THEN 'block' is invoked`() {
        val event = TestEvent()

        var actual = ""
        event.handle { actual = "block" }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN ViewEvent WHEN 'handle' called two times THEN second 'block' is not invoked`() {
        val event = TestEvent()

        var actual = ""
        event.handle { actual = "block" }
        event.handle { actual = "block2" }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN handled ViewEvent WHEN 'handle' called THEN 'block' is not invoked`() {
        val event = ViewEvent.None

        var actual = ""
        event.handle { actual = "block" }

        assertEquals("", actual)
    }
}
