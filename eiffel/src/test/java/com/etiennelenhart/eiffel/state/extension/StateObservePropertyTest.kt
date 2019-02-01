package com.etiennelenhart.eiffel.state.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.State
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class StateObservePropertyTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    data class TestState(val count: Int = 0, val other: String = "") : State

    @Test
    fun `GIVEN LiveData with State WHEN observed property changed THEN 'onChange' is invoked`() {
        val state = MutableLiveData<TestState>().apply { value = TestState() }

        var actual = 0
        state.observePropertyForever({ it.count }) { actual = it }
        state.value = state.value!!.copy(count = 1)

        assertEquals(1, actual)
    }

    @Test
    fun `GIVEN EiffelViewModel subclass WHEN 'dispatch' does not change observed property THEN 'onChange' is not invoked`() {
        val state = MutableLiveData<TestState>().apply { value = TestState() }

        var actual = 0
        state.observePropertyForever({ it.count }) { actual = it }
        state.value = state.value!!.copy(other = "changed")

        assertEquals(0, actual)
    }
}
