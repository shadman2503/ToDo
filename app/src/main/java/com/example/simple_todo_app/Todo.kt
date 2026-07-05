package com.example.simple_todo_app

import java.time.Instant
import  java.util.Date

data class Todo(
    var id: Int,
    var title: String,
    var createdAt: Date
)

fun getFakeTodo() : List<Todo>{
    return listOf<Todo>(
        Todo(1, "First Todo", Date.from(Instant.now())),
        Todo(2, "Second Todo", Date.from(Instant.now())),
        Todo(3, "Third Todo", Date.from(Instant.now())),
        Todo(4, "This will be my Forth Todo so that I can use in the UI", Date.from(Instant.now()))
    )
}
