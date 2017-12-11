package com.etiennelenhart.eiffel

import com.etiennelenhart.eiffel.livedata.Resource
import com.etiennelenhart.eiffel.result.Result

/**
 * Current status of a command's [Result] or a LiveData [Resource].
 */
enum class Status { SUCCESS, PENDING, ERROR }
