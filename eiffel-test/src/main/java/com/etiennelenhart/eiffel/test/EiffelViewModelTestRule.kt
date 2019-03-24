package com.etiennelenhart.eiffel.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.etiennelenhart.eiffel.Eiffel
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit Rule that sets the dispatchers used in [EiffelViewModel] to [Dispatchers.Unconfined] for synchronous execution and uses [InstantTaskExecutorRule]
 * for [LiveData] sources.
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
class EiffelViewModelTestRule : TestWatcher() {

    private val liveDataRule = object : InstantTaskExecutorRule() {
        public override fun starting(description: Description?) = super.starting(description)

        public override fun finished(description: Description?) = super.finished(description)
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Eiffel.setDispatchers(Dispatchers.Unconfined, Dispatchers.Unconfined)
        Dispatchers.setMain(Dispatchers.Unconfined)
        liveDataRule.starting(description)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Eiffel.resetDispatchers()
        Dispatchers.resetMain()
        liveDataRule.finished(description)
    }
}
