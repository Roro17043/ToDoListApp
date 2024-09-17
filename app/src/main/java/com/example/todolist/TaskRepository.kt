package com.example.todolist

import android.util.Log
import kotlinx.coroutines.flow.Flow


class TaskRepository(private val taskDao: TaskDao) {

    // Get all tasks (Room already returns a List<TaskEntity>)
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()

    // Insert a new task
    suspend fun insert(task: TaskEntity) {
        Log.d("TaskRepository", "Inserting task into database")
        taskDao.insertTask(task)
    }

    // Update a task
    suspend fun update(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    // Delete a task by ID
    suspend fun delete(taskId: Int) {
        taskDao.deleteTask(taskId)
    }
}
