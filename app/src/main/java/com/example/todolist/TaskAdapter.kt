package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class TaskAdapter(var taskList: MutableList<TaskEntity>, private val taskViewModel: TaskViewModel) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
   

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.textView)
        val taskDescription: TextView = itemView.findViewById(R.id.descriptionTextView)
        val taskCheckBox: CheckBox = itemView.findViewById(R.id.checkbox)
        val deleteIcon: ImageView = itemView.findViewById(R.id.trash_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.taskTitle.text = currentTask.title
        holder.taskDescription.text = currentTask.description

        // Temporarily remove the listener to avoid crashes while resetting the checkbox state
        holder.taskCheckBox.setOnCheckedChangeListener(null)

        // Set the checkbox state based on the current task's completion status
        holder.taskCheckBox.isChecked = currentTask.isDone

        // Re-attach the listener
        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            currentTask.isDone = isChecked

            // Display a Toast notification when the checkbox state changes
            val message = if (isChecked) {
                "${currentTask.title} marked as complete"
            } else {
                "${currentTask.title} marked as incomplete"
            }

            // Show the Toast message
            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()

            // Optional: Notify the adapter to refresh the item
            notifyItemChanged(position)
        }

        // Handle delete icon click to remove the task
        holder.deleteIcon.setOnClickListener {
            removeTask(position)
        }
    }




    override fun getItemCount() = taskList.size

    // Function to remove task from the list and update the RecyclerView
    private fun removeTask(position: Int) {
        // Delete task from the database using ViewModel
        val taskToDelete = taskList[position]

        taskViewModel.delete(taskToDelete.id) // Ensure this deletes from the database

        // Remove task from the list and notify the adapter
        taskList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, taskList.size)

    }
}
