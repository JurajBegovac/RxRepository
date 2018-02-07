package com.releaseit.rxrepository.app

import android.app.Activity
import android.app.Application
import android.content.Context
import com.releaseit.rxrepository.repository.local.realm.RealmDaggerModule
import com.releaseit.rxrepository.repository.local.sqlite.room.RoomModule
import com.releaseit.rxrepository.utils.LocalDb
import com.releaseit.rxrepository.utils.PREF_NAME
import com.releaseit.rxrepository.utils.ProcessPhoenix
import com.releaseit.rxrepository.utils.getLocalDb
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


/** Created by jurajbegovac on 27/01/2018. */

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        if (ProcessPhoenix.isPhoenixProcess(this))
            return

        appComponent = buildAppComponent()
        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    private fun buildAppComponent(): AppComponent {
        val localDb = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getLocalDb()
        return when (localDb) {
            LocalDb.SQLITE -> DaggerAppComponent.builder().application(this).roomModule(RoomModule(this)).build()
            LocalDb.REALM -> DaggerRealmAppComponent.builder().application(this).realmModule(RealmDaggerModule(this)).build()
        }
    }

    fun restart() {
        ProcessPhoenix.triggerRebirth(this)
    }
}
