package com.example.todolist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Convert the Flow from the repository to LiveData
    val allTasks = repository.allTasks.asLiveData()

    // Insert a new task
    fun insert(task: TaskEntity) = viewModelScope.launch(ioDispatcher) {
        Log.d("TaskViewModel", "Inserting task: ${task.title}")
        repository.insert(task)
    }

    // Update an existing task
    fun update(task: TaskEntity) = viewModelScope.launch(ioDispatcher) {
        repository.update(task)
    }

    // Delete a task by ID
    fun delete(taskId: Int) = viewModelScope.launch(ioDispatcher) {
        repository.delete(taskId)
    }


}
