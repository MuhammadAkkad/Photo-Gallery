package com.muhammed.aras.data.model

import java.util.Date

data class FilterModel(
    val query: String? = null,
    val dateRange: Pair<Date?, Date?>? = null
)