package com.example.routesapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.RoutesRepository
import com.example.routesapp.data.WorkoutsRepository
import com.example.shared.WorkoutSummary
import com.example.shared.WorkoutsQuery
import com.example.shared.WorkoutsQuerySortOrder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val workoutsRepository: WorkoutsRepository,
) : ViewModel() {
    private val _userWorkoutSummaries = MutableStateFlow(emptyList<WorkoutSummary>())
    private val _routeWorkoutSummaries = MutableStateFlow(emptyList<WorkoutSummary>())

    val userWorkouts = _userWorkoutSummaries.asStateFlow()
    val routeWorkouts = _routeWorkoutSummaries.asStateFlow()

    fun getFastestByRouteId(routeId: Int) {
        viewModelScope.launch {
            _routeWorkoutSummaries.value = workoutsRepository.getWorkouts(
                WorkoutsQuery(
                    route = routeId,
                    sort = WorkoutsQuerySortOrder.FASTEST
                )
            )
        }
    }

    fun getRecentByUser(user: String) {
        viewModelScope.launch {
            _userWorkoutSummaries.value = workoutsRepository.getWorkouts(
                WorkoutsQuery(
                    username = user,
                    sort = WorkoutsQuerySortOrder.RECENT
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WorkoutsViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.workoutsRepository,
                )
            }
        }
    }
}