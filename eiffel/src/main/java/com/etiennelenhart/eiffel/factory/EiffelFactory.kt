package com.etiennelenhart.eiffel.factory

import androidx.lifecycle.ViewModelProvider
import com.etiennelenhart.eiffel.viewmodel.EiffelViewModel

/**
 * Responsible for creating instances of [EiffelViewModel].
 */
interface EiffelFactory : ViewModelProvider.Factory {

    /**
     * Simple factory that calls the view model's empty constructor.
     */
    object Default : EiffelFactory, ViewModelProvider.NewInstanceFactory()
}
