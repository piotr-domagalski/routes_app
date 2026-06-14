package com.example.routesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.routesapp.RoutesApp
import com.example.routesapp.data.RoutesRepository
import com.example.shared.ActivityType
import com.example.shared.RouteType
import com.example.shared.RoutesQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RouteSearchViewModel(
    private val repository: RoutesRepository
) : ViewModel() {
    val showFilters = MutableStateFlow(false)
    val routeType = MutableStateFlow<RouteType?>(null)
    val activityType = MutableStateFlow<ActivityType?>(null)

    private val _searchString = MutableStateFlow<String?>(null)
    val searchString = _searchString.asStateFlow()
    val distanceMin = 0
    val distanceMax = 0

    fun setSearchString(new: String?) {
        if (new.isNullOrBlank()) {
            _searchString.value = null
        } else {
            _searchString.value = new
        }
    }

    fun getQuery(offset: Long, count: Int): RoutesQuery {
        return RoutesQuery(
            search = searchString.value,
            activityType = activityType.value,
            routeType = routeType.value,
            offset = offset,
            count = count
        )
    }

    fun updateQuery(offset: Long, count: Int) {
        repository.query.value = getQuery(offset, count)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RouteSearchViewModel(
                    (this[APPLICATION_KEY] as RoutesApp).appContainer.routesRepository
                )
            }
        }
    }
}