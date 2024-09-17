package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.AlertDialog
import android.widget.EditText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<TaskEntity>()
    private lateinit var fab: FloatingActionButton

    // ViewModel initialization without ViewModelFactory
    private val taskViewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    val db = Room.databaseBuilder(applicationContext, TaskDatabase::class.java, "task-db").build()
                    return TaskViewModel(TaskRepository(db.taskDao()), ioDispatcher = Dispatchers.IO) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList, taskViewModel)
        recyclerView.adapter = taskAdapter

        // Observe the Flow directly from the ViewModel
        // Directly observe the LiveData
        taskViewModel.allTasks.observe(this) { tasks ->
            taskList.clear()
            taskList.addAll(tasks)
            taskAdapter.notifyDataSetChanged()
        }





        // Floating Action Button to add a new task
        fab = findViewById(R.id.fab_add_task)
        fab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    // Function to show the dialog to add a new task
    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add New Task")
            .setPositiveButton("Add") { dialogInterface, i ->
                val taskTitle = dialogView.findViewById<EditText>(R.id.editTextTaskTitle).text.toString()
                val taskDescription = dialogView.findViewById<EditText>(R.id.editTextTaskDescription).text.toString()

                if (taskTitle.isNotEmpty()) {
                    val newTask = TaskEntity(title = taskTitle, description = taskDescription, isDone = false)
                    // Insert the new task into the database
                    taskViewModel.insert(newTask)
                }
            }
            .setNegativeButton("Cancel") { dialogInterface, i ->
                dialogInterface.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
