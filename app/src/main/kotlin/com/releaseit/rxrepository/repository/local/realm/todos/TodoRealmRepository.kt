package com.releaseit.rxrepository.repository.local.realm.todos

import com.releaseit.rxrepository.repository.local.realm.RealmRepository
import com.releaseit.rxrepository.repository.local.todos.Todo
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.funktionale.option.Option
import java.util.*

/** Created by jurajbegovac on 26/01/2018. */

/**
 * RealmTodo object
 */
open class RealmTodo(@PrimaryKey var id: String = UUID.randomUUID().toString(), var title: String = "",
                     var message: String = "") : RealmObject()

/**
 * TodoItem realm repository
 */
class TodoRealmRepository : TodoRepository, RealmRepository<Todo, RealmTodo>({ it.realm }) {

  override fun observeFirstWith(title: String) = get(createQuerySpecification({
                                                                                it.where(RealmTodo::class.java)
                                                                                  .equalTo("title", title)
                                                                                  .findAll()
                                                                              }, {
                                                                                if (it.isEmpty()) {
                                                                                  Option.empty<Todo>()
                                                                                } else {
                                                                                  Option.Some(it[0]!!.todo)
                                                                                }
                                                                              }))

  override fun all() = query(createQuerySpecification({
                                                        it.where(RealmTodo::class.java)
                                                          .findAll()
                                                      }, {
                                                        if (it.isEmpty()) {
                                                          emptyList()
                                                        } else {
                                                          it.map { it.todo }
                                                        }
                                                      }))
}

/**
 * Mappers from TodoItem to RealmTodo and vice versa
 */
internal val Todo.realm: RealmTodo
  get() = RealmTodo(id, title, message)

internal val RealmTodo.todo: Todo
  get() = Todo(id, title, message)
