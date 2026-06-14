package com.example.shared

import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class RoutesQuery(
    val search: String? = null,
    val activityType: ActivityType? = DEFAULT_ACTIVITY_TYPE,
    val routeType: RouteType? = DEFAULT_ROUTE_TYPE,
    val count: Int = DEFAULT_COUNT,
    val offset: Long = DEFAULT_OFFSET
) {
    fun withBounds(count: Int? = null, offset: Long? = null): RoutesQuery {
        return RoutesQuery(
            search = this.search,
            activityType = this.activityType,
            routeType = this.routeType,
            count = count ?: this.count,
            offset = offset ?: this.offset,

        )
    }
    companion object {
        val DEFAULT_ACTIVITY_TYPE = null
        val DEFAULT_ROUTE_TYPE = null
        const val DEFAULT_COUNT: Int = 10
        const val DEFAULT_OFFSET: Long = 0
    }
}
