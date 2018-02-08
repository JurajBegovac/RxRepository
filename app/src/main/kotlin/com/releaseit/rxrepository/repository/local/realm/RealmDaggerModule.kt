package com.releaseit.rxrepository.repository.local.realm

import android.content.Context
import com.releaseit.rxrepository.dagger2.qualifiers.ApplicationContext
import com.releaseit.rxrepository.repository.local.realm.todos.TodoRealmRepository
import com.releaseit.rxrepository.repository.local.todos.TodoRepository
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

/**
 * Created by jurajbegovac on 02/02/2018.
 */
@Module
class RealmDaggerModule(@ApplicationContext private val context: Context) {

  init {
    Realm.init(context)
    val realmDefaultConfig = RealmConfiguration.Builder()
      .schemaVersion(1)
      .build()
    Realm.setDefaultConfiguration(realmDefaultConfig)
  }

  @Singleton
  @Provides
  fun todoRepository(): TodoRepository = TodoRealmRepository()
}
