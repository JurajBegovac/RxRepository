package com.releaseit.rxrepository.app

import com.releaseit.rxrepository.dagger2.qualifiers.PerActivity
import com.releaseit.rxrepository.todos.TodoActivity
import com.releaseit.rxrepository.todos.TodosModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by jurajbegovac on 27/01/2018.
 */
@Module
abstract class ActivityBuilder {

  @PerActivity
  @ContributesAndroidInjector
  abstract fun bindMainActivity(): MainActivity

  @PerActivity
  @ContributesAndroidInjector(modules = [TodosModule::class])
  abstract fun bindTodoActivity(): TodoActivity
}
