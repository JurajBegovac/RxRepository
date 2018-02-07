package com.releaseit.rxrepository.repository.local.todos

import com.releaseit.rxrepository.repository.local.Repository
import io.reactivex.Flowable
import org.funktionale.option.Option
import java.util.*

/**
 * Created by jurajbegovac on 27/01/2018.
 */

data class Todo(val id: String = UUID.randomUUID().toString(), val title: String, val message: String)

interface TodoRepository : Repository<Todo> {
    fun all(): Flowable<List<Todo>>

    fun observeFirstWith(title: String): Flowable<Option<Todo>>
}
