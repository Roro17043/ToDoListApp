package com.example.todolist

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Retrieve all tasks
    @Query("SELECT * FROM tasks")
     fun getAllTasks(): Flow<List<TaskEntity>>

    // Insert a new task
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    // Update a task (e.g., marking it as done or not)
    @Update
    suspend fun updateTask(task: TaskEntity)

    // Delete a task
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Int)



}
