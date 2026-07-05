package com.example.simple_todo_app

import java.util.Date
import kotlin.time.ExperimentalTime
import java.time.Instant

object TodoManager {
    private val todoList = mutableListOf<Todo>()

    fun getAllTodo(): List<Todo> {
        return todoList
    }

    @OptIn(ExperimentalTime::class)
    fun addTodo(title: String) {
        todoList.add(Todo(System.currentTimeMillis().toInt(), title,Date.from(Instant.now())))
    }

    fun deleteTodo(id: Int) {
        todoList.removeIf {
            it.id == id
        }
    }
}