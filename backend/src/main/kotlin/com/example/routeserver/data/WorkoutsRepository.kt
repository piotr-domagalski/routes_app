package com.example.routeserver.data

import com.example.shared.WorkoutSummary
import com.example.shared.WorkoutsQuery
import com.example.shared.WorkoutsQuerySortOrder
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.not
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class WorkoutsRepository {
    fun getWorkouts(query: WorkoutsQuery, session: Session): List<WorkoutSummary> {
        return transaction {
            var q = WorkoutsTable.selectAll()

            val route = query.route
            route?.let {
                q = q.andWhere { WorkoutsTable.route eq route}
            }
            val user = query.username
            user?.let {
                q.andWhere { WorkoutsTable.user eq user }
            }
            q.andWhere { WorkoutsTable.user eq session.user or (not(WorkoutsTable.private)) }

            when (query.sort) {
                WorkoutsQuerySortOrder.RECENT -> q.orderBy(WorkoutsTable.timestamp to SortOrder.DESC)
                WorkoutsQuerySortOrder.FASTEST -> q.orderBy(WorkoutsTable.duration to SortOrder.ASC)
            }
            q.limit(query.count)
            .offset(query.offset)
            q.map { WorkoutSummary(
                user = it[WorkoutsTable.user],
                route = it[WorkoutsTable.route],
                timestamp = it[WorkoutsTable.timestamp],
                duration = it[WorkoutsTable.duration],
                private = it[WorkoutsTable.private]
            )}
        }
    }

    fun insertWorkout(workout: WorkoutSummary) {
        transaction {
            addLogger(StdOutSqlLogger)
            WorkoutsTable.insert {
                it[WorkoutsTable.user] = workout.user
                it[WorkoutsTable.route] = workout.route
                it[WorkoutsTable.timestamp] = workout.timestamp
                it[WorkoutsTable.duration] = workout.duration
                it[WorkoutsTable.private] = workout.private
            }
        }
    }

    fun deleteWorkout(): Boolean {
        TODO()
    }
}
