package com.releaseit.rxrepository.repository.local.sqlite.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.releaseit.rxrepository.repository.local.sqlite.room.todos.RoomTodo
import com.releaseit.rxrepository.repository.local.sqlite.room.todos.TodoDao

/**
 * Created by jurajbegovac on 27/01/2018.
 */

@Database(entities = [RoomTodo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun todoDao(): TodoDao
}
