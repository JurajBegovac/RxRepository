package com.releaseit.rxrepository.repository.local.sqlite.room

import android.arch.persistence.room.Room
import android.content.Context
import com.releaseit.rxrepository.dagger2.qualifiers.ApplicationContext
import com.releaseit.rxrepository.repository.local.sqlite.room.todos.TodoDao
import com.releaseit.rxrepository.repository.local.sqlite.room.todos.TodoRoomRepository
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by jurajbegovac on 02/02/2018.
 */

@Module
class RoomModule(@ApplicationContext private val context: Context) {

    @Singleton
    @Provides
    fun providesAppDatabase(): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "rx-repo-db")
                    .build()

    @Singleton
    @Provides
    fun todoDao(database: AppDatabase) = database.todoDao()


    @Singleton
    @Provides
    fun todoRepository(todoDao: TodoDao): TodoRepository = TodoRoomRepository(todoDao)

}
