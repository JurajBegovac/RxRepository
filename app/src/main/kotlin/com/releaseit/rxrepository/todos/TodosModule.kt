package com.releaseit.rxrepository.todos

import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import dagger.Module
import dagger.Provides

/**
 * Created by jurajbegovac on 09/02/2018.
 */
@Module
class TodosModule {

  @Provides
  fun todoViewModelFactory(todoRepository: TodoRepository) = TodoViewModelFactory(todoRepository)
}
