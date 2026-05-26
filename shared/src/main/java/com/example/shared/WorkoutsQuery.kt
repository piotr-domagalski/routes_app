package com.example.shared

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutsQuery(
    val username: String? = null,
    val route: Int? = null,
    val sort: WorkoutsQuerySortOrder = DEFAULT_SORT,
    val count: Int = DEFAULT_COUNT,
    val offset: Long = DEFAULT_OFFSET
) {
    companion object {
        val DEFAULT_SORT = WorkoutsQuerySortOrder.RECENT
        const val DEFAULT_COUNT: Int = 10
        const val DEFAULT_OFFSET: Long = 0

    }
}
