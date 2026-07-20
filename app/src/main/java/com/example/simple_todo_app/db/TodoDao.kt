package com.example.simple_todo_app.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.simple_todo_app.Todo
import java.util.Date

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo WHERE deletedAt IS NULL ORDER BY createdAt DESC")
    fun getAllTodo() : LiveData<List<Todo>>

    @Query("SELECT * FROM Todo WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC")
    fun getTrashedTodos() : LiveData<List<Todo>>

    @Insert
    fun addTodo(todo: Todo)

    @Update
    fun updateTodo(todo: Todo)

    @Query("UPDATE Todo SET deletedAt = :deletedAt WHERE id = :id")
    fun moveToTrash(id: Int, deletedAt: Date)

    @Query("UPDATE Todo SET deletedAt = NULL WHERE id = :id")
    fun restoreFromTrash(id: Int)

    @Query("DELETE FROM Todo WHERE id = :id")
    fun deletePermanently(id: Int)

    @Query("DELETE FROM Todo WHERE deletedAt < :threshold")
    fun deleteOldTrashedTodos(threshold: Date)
}
