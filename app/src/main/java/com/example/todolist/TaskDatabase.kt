package com.example.todolist

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    // 2. Abstract Functions
    abstract fun taskDao(): TaskDao
}
