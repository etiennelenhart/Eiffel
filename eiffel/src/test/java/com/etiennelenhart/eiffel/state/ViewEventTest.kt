package com.etiennelenhart.eiffel.state

import org.junit.Test
import kotlin.test.assertEquals

class ViewEventTest {

    class TestEvent : ViewEvent()

    @Test
    fun `GIVEN ViewEvent WHEN 'peek' called THEN 'block' is invoked`() {
        val event = TestEvent()

        var actual = ""
        event.peek {
            actual = "block"
            true
        }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN ViewEvent WHEN 'peek' called two times and first was able to handle THEN second 'block' is not invoked`() {
        val event = TestEvent()

        var actual = ""
        event.peek {
            actual = "block"
            true
        }
        event.peek {
            actual = "second block"
            true
        }

        assertEquals("block", actual)
    }

    @Test
    fun `GIVEN ViewEvent WHEN 'peek' called two times and first was not able to handle THEN second 'block' is invoked`() {
        val event = TestEvent()

        var actual = ""
        event.peek { false }
        event.peek {
            actual = "second block"
            true
        }

        assertEquals("second block", actual)
    }
}
