package com.etiennelenhart.eiffel.result

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AttemptTest {

    object TestException : Exception()

    @Test
    fun `GIVEN attempt with 'block' WHEN invoked THEN Result contains block's result`() {
        val given = { attempt { 1 } }

        val actual = (given() as Result.Success).data

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN attempt with faulty 'block' WHEN invoked THEN Result contains AttemptError`() {
        val given = { attempt { throw TestException } }

        val actual = (given() as Result.Error).type is AttemptError

        assertTrue(actual)
    }

    @Test
    fun `GIVEN attempt with faulty 'block' WHEN invoked THEN AttemptError contains exception`() {
        val given = { attempt { throw TestException } }

        val actual = ((given() as Result.Error).type as AttemptError).exception

        assertEquals(actual, TestException)
    }
}
