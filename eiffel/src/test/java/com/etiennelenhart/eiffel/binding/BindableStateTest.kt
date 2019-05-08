package com.etiennelenhart.eiffel.binding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.state.State
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BindableStateTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    data class PrimaryTestState(val count: Int = 0) : State
    data class SecondaryTestState(val count: Int = 0) : State
    data class TertiaryTestState(val count: Int = 0) : State

    class BindableTestState(
        primaryState: LiveData<PrimaryTestState>,
        secondaryState: LiveData<SecondaryTestState>,
        tertiaryState: LiveData<TertiaryTestState>
    ) : BindableState<PrimaryTestState>(primaryState) {

        val singleEmptyStateVisible by bindableProperty(true) { it.count == 0 }

        val doubleEmptyStateVisible by bindableProperty(true, secondaryState) { primary, secondary -> primary.count == 0 && secondary.count == 0 }

        val tripleEmptyStateVisible by bindableProperty(true, secondaryState, tertiaryState) { primary, secondary, tertiary ->
            primary.count == 0 && secondary.count == 0 && tertiary.count == 0
        }
    }

    @Test
    fun `GIVEN BindableState property with 'state' and 'initialValue' WHEN observed THEN property emits 'initialValue'`() {
        val primaryState = MutableLiveData<PrimaryTestState>()
        val secondaryState = MutableLiveData<SecondaryTestState>()
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = false
        bindableState.singleEmptyStateVisible.observeForever { actual = it }

        assertTrue(actual)
    }

    @Test
    fun `GIVEN BindableState property with 'state' WHEN state changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>()
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.singleEmptyStateVisible.observeForever { actual = it }
        primaryState.value = primaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN BindableState property with two 'states' and 'initialValue' WHEN observed THEN property emits 'initialValue'`() {
        val primaryState = MutableLiveData<PrimaryTestState>()
        val secondaryState = MutableLiveData<SecondaryTestState>()
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = false
        bindableState.doubleEmptyStateVisible.observeForever { actual = it }

        assertTrue(actual)
    }

    @Test
    fun `GIVEN BindableState property with two 'states' WHEN first changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>().apply { value = SecondaryTestState() }
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.doubleEmptyStateVisible.observeForever { actual = it }
        primaryState.value = primaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN BindableState property with two 'states' WHEN second changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>().apply { value = SecondaryTestState() }
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.doubleEmptyStateVisible.observeForever { actual = it }
        secondaryState.value = secondaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN BindableState property with three 'states' and 'initialValue' WHEN observed THEN property emits 'initialValue'`() {
        val primaryState = MutableLiveData<PrimaryTestState>()
        val secondaryState = MutableLiveData<SecondaryTestState>()
        val tertiaryState = MutableLiveData<TertiaryTestState>()
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = false
        bindableState.tripleEmptyStateVisible.observeForever { actual = it }

        assertTrue(actual)
    }

    @Test
    fun `GIVEN BindableState property with three 'states' WHEN first changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>().apply { value = SecondaryTestState() }
        val tertiaryState = MutableLiveData<TertiaryTestState>().apply { value = TertiaryTestState() }
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.tripleEmptyStateVisible.observeForever { actual = it }
        primaryState.value = primaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN BindableState property with three 'states' WHEN second changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>().apply { value = SecondaryTestState() }
        val tertiaryState = MutableLiveData<TertiaryTestState>().apply { value = TertiaryTestState() }
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.tripleEmptyStateVisible.observeForever { actual = it }
        secondaryState.value = secondaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN BindableState property with three 'states' WHEN third changed THEN property emits mapped value`() {
        val primaryState = MutableLiveData<PrimaryTestState>().apply { value = PrimaryTestState() }
        val secondaryState = MutableLiveData<SecondaryTestState>().apply { value = SecondaryTestState() }
        val tertiaryState = MutableLiveData<TertiaryTestState>().apply { value = TertiaryTestState() }
        val bindableState = BindableTestState(primaryState, secondaryState, tertiaryState)

        var actual = true
        bindableState.tripleEmptyStateVisible.observeForever { actual = it }
        tertiaryState.value = tertiaryState.value!!.copy(count = 1)

        assertFalse(actual)
    }
}
