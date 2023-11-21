package com.team2.todo.screens.details_page.view_model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team2.todo.data.entities.Images
import com.team2.todo.data.entities.SubTodo
import com.team2.todo.data.entities.Todo
import com.team2.todo.data.entities.relations.TodoWithSubTodos
import com.team2.todo.data.repo.SubTodoRepo
import com.team2.todo.data.repo.TodoRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailsPageViewModel(private val repo: TodoRepo, private val subTodoRepo: SubTodoRepo) : ViewModel() {


    var todosList = repo.getAllTodosWithSubTodos();

    fun getPropertyFromId(propertyId: Long):Flow<List<TodoWithSubTodos>>{
        return  repo.getTodoWithSubTodosBasedOnTodoId(propertyId)
    }

    fun updateTodo(todoId: Long, status: Boolean): Unit{
        viewModelScope.launch {
            repo.updateTodoStatus(todoId,status)
        }

    }

    fun getTodoImages(id: Long) : Flow<List<Images>>{
        return repo.getAllTodoImagesBasedOnTodo(id)
    }







    }


