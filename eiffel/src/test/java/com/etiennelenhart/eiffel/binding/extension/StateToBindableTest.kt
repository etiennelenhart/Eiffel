package com.etiennelenhart.eiffel.binding.extension

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.etiennelenhart.eiffel.binding.BindableState
import com.etiennelenhart.eiffel.binding.bindableMapping
import com.etiennelenhart.eiffel.state.State
import com.etiennelenhart.eiffel.view.EiffelFragment
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class StateToBindableTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    data class FirstTestState(val count: Int = 0) : State
    data class SecondTestState(val count: Int = 0) : State
    data class ThirdTestState(val count: Int = 0) : State

    data class BindableTestState(val emptyStateVisible: Boolean = false) : BindableState

    private val mapping1 = bindableMapping<FirstTestState, BindableTestState> { state ->
        copy(emptyStateVisible = state.count == 0)
    }

    private val mapping2 = bindableMapping<FirstTestState, SecondTestState, BindableTestState> { firstState, secondState ->
        copy(emptyStateVisible = firstState.count == 0 && secondState.count == 0)
    }

    private val mapping3 = bindableMapping<FirstTestState, SecondTestState, ThirdTestState, BindableTestState> { firstState, secondState, thirdState ->
        copy(emptyStateVisible = firstState.count == 0 && secondState.count == 0 && thirdState.count == 0)
    }

    @Test
    fun `GIVEN LiveData with State WHEN value queried THEN initial value is returned`() {
        val expected = BindableTestState()
        val state = MutableLiveData<FirstTestState>()

        val actual = state.toBindable(mapping1).value

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN LiveData with State WHEN value changed THEN mapped value is emitted`() {
        val state = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }

        var actual = true
        state.toBindable(mapping1).observeForever { actual = it.emptyStateVisible }
        state.value = state.value!!.copy(count = 1)

        assertFalse(actual)
    }

    class TestFragment : EiffelFragment()

    private val testFragment = TestFragment()

    @Test
    fun `GIVEN two LiveData with State WHEN value queried THEN initial value is returned`() {
        val expected = BindableTestState()
        val firstState = MutableLiveData<FirstTestState>()
        val secondState = MutableLiveData<SecondTestState>()

        val actual = testFragment.bindableStateSource(firstState, secondState).toBindable(mapping2).value

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN two LiveData with State WHEN first changed THEN mapped value is emitted`() {
        val firstState = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }
        val secondState = MutableLiveData<SecondTestState>().apply { value = SecondTestState() }

        var actual = true
        testFragment.bindableStateSource(firstState, secondState).toBindable(mapping2).observeForever { actual = it.emptyStateVisible }
        firstState.value = firstState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN two LiveData with State WHEN second changed THEN mapped value is emitted`() {
        val firstState = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }
        val secondState = MutableLiveData<SecondTestState>().apply { value = SecondTestState() }

        var actual = true
        testFragment.bindableStateSource(firstState, secondState).toBindable(mapping2).observeForever { actual = it.emptyStateVisible }
        secondState.value = secondState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN three LiveData with State WHEN value queried THEN initial value is returned`() {
        val expected = BindableTestState()
        val firstState = MutableLiveData<FirstTestState>()
        val secondState = MutableLiveData<SecondTestState>()
        val thirdState = MutableLiveData<ThirdTestState>()

        val actual = testFragment.bindableStateSource(firstState, secondState, thirdState).toBindable(mapping3).value

        assertEquals(expected, actual)
    }

    @Test
    fun `GIVEN three LiveData with State WHEN first changed THEN mapped value is emitted`() {
        val firstState = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }
        val secondState = MutableLiveData<SecondTestState>().apply { value = SecondTestState() }
        val thirdState = MutableLiveData<ThirdTestState>().apply { value = ThirdTestState() }

        var actual = true
        testFragment.bindableStateSource(firstState, secondState, thirdState).toBindable(mapping3).observeForever { actual = it.emptyStateVisible }
        firstState.value = firstState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN three LiveData with State WHEN second changed THEN mapped value is emitted`() {
        val firstState = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }
        val secondState = MutableLiveData<SecondTestState>().apply { value = SecondTestState() }
        val thirdState = MutableLiveData<ThirdTestState>().apply { value = ThirdTestState() }

        var actual = true
        testFragment.bindableStateSource(firstState, secondState, thirdState).toBindable(mapping3).observeForever { actual = it.emptyStateVisible }
        secondState.value = secondState.value!!.copy(count = 1)

        assertFalse(actual)
    }

    @Test
    fun `GIVEN three LiveData with State WHEN third changed THEN mapped value is emitted`() {
        val firstState = MutableLiveData<FirstTestState>().apply { value = FirstTestState() }
        val secondState = MutableLiveData<SecondTestState>().apply { value = SecondTestState() }
        val thirdState = MutableLiveData<ThirdTestState>().apply { value = ThirdTestState() }

        var actual = true
        testFragment.bindableStateSource(firstState, secondState, thirdState).toBindable(mapping3).observeForever { actual = it.emptyStateVisible }
        thirdState.value = thirdState.value!!.copy(count = 1)

        assertFalse(actual)
    }
}
