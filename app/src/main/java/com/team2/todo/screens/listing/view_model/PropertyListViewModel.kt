package com.team2.todo.screens.listing.view_model

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team2.todo.common_ui_components.ReminderModel
import com.team2.todo.common_ui_components.filter.view_model.Filter
import com.team2.todo.common_ui_components.filter.view_model.FilterViewModel
import com.team2.todo.data.entities.relations.TodoWithSubTodos
import com.team2.todo.data.repo.TodoRepo
import com.team2.todo.utils.GeoFenceUtil
import com.team2.todo.utils.LocationUtil
import com.team2.todo.utils.NotificationUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Created by Manu KJ on 11/14/23.
 */


object ListingViewModel {
    private lateinit var repo: TodoRepo
    var instance: PropertyListViewModel? = null

    fun getInstance(
        repo: TodoRepo,
        filterViewModel: FilterViewModel,
        ctx: Context
    ): PropertyListViewModel {
        if (instance == null) {
            this.repo = repo
            instance = PropertyListViewModel(repo, filterViewModel, ctx)
        }
        return instance!!
    }

}

class PropertyListViewModel(
    val repo: TodoRepo,
    var filterViewModel: FilterViewModel,
    var ctx: Context
) :
    ViewModel() {
    private var isNotificationShown = false
    private val THRESHOLD_DISTANCE = 500.0 // 500 in meters
    var inSalePropertyList = MutableStateFlow<List<TodoWithSubTodos>>(emptyList())
    var completedPropertyList = MutableStateFlow<List<TodoWithSubTodos>>(emptyList())
    var reminderModel by mutableStateOf<ReminderModel?>(null);


    init {
        fetchUpdatedList()
        fetchClosestTask()
        fetchNearestTask()
    }

    private fun fetchClosestTask() {
        var count = 0
        viewModelScope.launch {
            delay(3000)
            var closestDuePropertyIndex = -1
            var previousClosestDateDifference = Long.MAX_VALUE
            val MINUTES_IN_DAY = 360
            inSalePropertyList.value.forEachIndexed { index, it ->
                val dueDateTodo = it.todo.dueDate
                val currentDate = LocalDateTime.now()

                if (dueDateTodo != null) {
                    val currentDueDateDifference =
                        ChronoUnit.MINUTES.between(dueDateTodo, currentDate)

                    if (currentDueDateDifference >= 0 && currentDueDateDifference <= MINUTES_IN_DAY) {
                        count++
                        if (currentDueDateDifference < previousClosestDateDifference) {
                            previousClosestDateDifference = currentDueDateDifference
                            closestDuePropertyIndex = index
                        }
                    }
                } else {
                    println("Fetched Due date is empty for property at index $index!!")
                }
            }
            if (closestDuePropertyIndex != -1) {
                reminderModel =
                    ReminderModel(count, inSalePropertyList.value[closestDuePropertyIndex])
            }
        }
    }

    fun fetchNearestTask() {
        if (!isNotificationShown) {
            if (LocationUtil.valid()) {
                run {
                    inSalePropertyList.value.forEach { it ->
                        run {

                            if (((it.todo.latitude
                                    ?: 0.0) != 0.0 || (it.todo.longitude
                                    ?: 0.0) != 0.0)
                            ) {
                                var distance = GeoFenceUtil.calculateDistance(
                                    it.todo.latitude ?: 0.0,
                                    it.todo.longitude ?: 0.0,
                                    LocationUtil.currentLocation!!
                                )
                                if (distance >= THRESHOLD_DISTANCE) {
                                    isNotificationShown = true
                                    NotificationUtil.showGeoFencingNotification(
                                        property = it
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    fun fetchUpdatedList() {
        getDataForSelectedFilter(filterViewModel.selectedFilter.value)
    }

    fun updateStatus(todoId: Long, status: Boolean): Boolean {
        viewModelScope.launch {
            repo.updateTodoStatus(todoId, status)
            delay(300)
            fetchUpdatedList()
        }
        return true;
    }

    fun getDataForSelectedFilter(selectedFilter: Filter) {

        fetchDataForSelectedFilter(selectedFilter, false) {
            inSalePropertyList.value = it
        }

        fetchDataForSelectedFilter(selectedFilter, true) {
            completedPropertyList.value = it
        }
    }

    private fun fetchDataForSelectedFilter(
        selectedFilter: Filter,
        status: Boolean,
        callback: (List<TodoWithSubTodos>) -> Unit
    ) {

        when (selectedFilter) {

            Filter.DEFAULT_FILTER -> {
                viewModelScope.launch {
                    repo.getAllTodosWithSubTodos(status = status).collect { list ->
                        callback(list)
                    }
                }
            }

            Filter.DUE_DATE -> {
                viewModelScope.launch {
                    repo.getAllTodosOrderedByDueDateDESCWithSubTodos(status = status)
                        .collect { list ->
                            callback(list)
                        }
                }
            }

            Filter.HIGH_PRIORITY -> {
                viewModelScope.launch {
                    repo.getAllTodosOrderedByPriorityDESCWithSubTodos(status = status)
                        .collect { list ->
                            callback(list)
                        }
                }
            }

            Filter.LOW_PRIORITY -> {
                viewModelScope.launch {
                    repo.getAllTodosOrderedByPriorityASCWithSubTodos(status = status)
                        .collect { list ->
                            callback(list)
                        }
                }
            }

            Filter.GEO_LOCATION -> {
                viewModelScope.launch {
                    repo.getAllTodosWithSubTodos(status = status).collect { list ->
                        if (LocationUtil.valid()) {
                            callback(
                                GeoFenceUtil.sortLocationByDistance(
                                    list,
                                    LocationUtil.currentLocation!!
                                )
                            )
                        }
                    }
                }
            }

            Filter.HIGH_PRICE -> {
                viewModelScope.launch {
                    repo.getAllTodosOrderedByPriceDESCWithSubTodos(status = status)
                        .collect { list ->
                            callback(list)
                        }
                }
            }

            Filter.LOW_PRICE -> {
                viewModelScope.launch {
                    repo.getAllTodosOrderedByPriceASCWithSubTodos(status = status).collect { list ->
                        callback(list)
                    }
                }
            }
        }

    }

    fun deleteTheProperty(todoId: Long) {
        viewModelScope.launch {
            repo.deleteProperty(todoId)
        }
    }
}