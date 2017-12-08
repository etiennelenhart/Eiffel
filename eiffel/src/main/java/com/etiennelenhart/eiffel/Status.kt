package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.livedata.Resource
import com.etiennelenhart.eiffel.result.Result

/**
 * The current status of a command's [Result] or LiveData [Resource].
 */
enum class Status { SUCCESS, PENDING, ERROR }
