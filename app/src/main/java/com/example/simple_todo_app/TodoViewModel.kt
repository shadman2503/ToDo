package com.example.simple_todo_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import java.util.Calendar

class TodoViewModel: ViewModel() {

    private val todoDao = MainApplication.todoDatabase.getTodoDao()
    val todoList : LiveData<List<Todo>> = todoDao.getAllTodo()
    val trashedTodos : LiveData<List<Todo>> = todoDao.getTrashedTodos()

    init {
        cleanOldTrash()
    }

    private fun cleanOldTrash() {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            todoDao.deleteOldTrashedTodos(calendar.time)
        }
    }

    fun addTodo(title: String, description: String) {
        viewModelScope.launch (Dispatchers.IO ) {
            todoDao.addTodo(Todo(title = title, description = description, createdAt = Date.from(Instant.now())))
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.updateTodo(todo)
        }
    }

    fun moveToTrash(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.moveToTrash(id, Date.from(Instant.now()))
        }
    }

    fun restoreTodo(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.restoreFromTrash(id)
        }
    }

    fun deletePermanently(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoDao.deletePermanently(id)
        }
    }
}
