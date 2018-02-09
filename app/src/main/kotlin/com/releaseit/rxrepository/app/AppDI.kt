package com.releaseit.rxrepository.app

import android.content.Context
import com.releaseit.rxrepository.dagger2.qualifiers.ApplicationContext
import com.releaseit.rxrepository.repository.local.realm.RealmDaggerModule
import com.releaseit.rxrepository.repository.local.sqlite.room.RoomModule
import com.releaseit.rxrepository.utils.PREF_NAME
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by jurajbegovac on 27/01/2018.
 */

@Module
class AppModule {

  @Provides
  @Singleton
  @ApplicationContext
  fun context(app: App): Context {
    return app
  }

  @Provides
  @Singleton
  fun sharedPrefs(@ApplicationContext context: Context) = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

}

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  ActivityBuilder::class,
  AppModule::class,
  RoomModule::class
])
interface AppComponent {

  fun inject(target: App)

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(app: App): Builder

    fun roomModule(roomModule: RoomModule): Builder

    fun build(): AppComponent
  }
}

@Singleton
@Component(modules = [
  AndroidSupportInjectionModule::class,
  ActivityBuilder::class,
  AppModule::class,
  RealmDaggerModule::class
])
interface RealmAppComponent : AppComponent {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(app: App): Builder

    fun realmModule(realmModule: RealmDaggerModule): Builder

    fun build(): AppComponent
  }
}
