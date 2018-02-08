package com.releaseit.rxrepository.repository.local.sqlite.room.todos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update
import com.releaseit.rxrepository.repository.local.todos.Todo
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import io.reactivex.Flowable
import org.funktionale.option.Option
import java.util.*

/**
 * Created by jurajbegovac on 27/01/2018.
 */

@Entity(tableName = "todo")
data class RoomTodo(@PrimaryKey var id: String = UUID.randomUUID().toString(), var title: String, var message: String)

class TodoRoomRepository(private val todoDao: TodoDao) : TodoRepository {

  override fun insert(item: Todo) {
    todoDao.insert(item.room)
  }

  override fun insert(items: List<Todo>) {
    todoDao.insert(items.map { it.room })
  }

  override fun update(item: Todo) {
    todoDao.update(item.room)
  }

  override fun upsert(item: Todo) {
    todoDao.upsert(item.room)
  }

  override fun delete(item: Todo) {
    todoDao.delete(item.room)
  }

  override fun all(): Flowable<List<Todo>> = todoDao.all().map { it.map { it.todo } }

  override fun observeFirstWith(title: String): Flowable<Option<Todo>> = todoDao.withName(title).map {
    if (it.isEmpty()) {
      Option.empty<Todo>()
    } else {
      Option.Some(it[0].todo)
    }
  }
}

/**
 * Mappers from TodoItem to RealmTodo and vice versa
 */
internal val RoomTodo.todo: Todo
  get() = Todo(id, title, message)

internal val Todo.room: RoomTodo
  get() = RoomTodo(id, title, message)

/**
 * Room DAO
 */
@Dao
abstract class TodoDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract fun insert(todo: RoomTodo)

  @Insert
  abstract fun insert(todos: List<RoomTodo>)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  abstract fun update(todo: RoomTodo)

  @Delete
  abstract fun delete(todo: RoomTodo)

  @Query("SELECT * FROM todo")
  abstract fun all(): Flowable<List<RoomTodo>>

  @Query("SELECT * FROM todo WHERE title == :title")
  abstract fun withName(title: String): Flowable<List<RoomTodo>>

  @Transaction
  open fun upsert(todo: RoomTodo) {
    insert(todo)
    update(todo)
  }

}
