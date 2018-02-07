package com.releaseit.rxrepository.app

import com.releaseit.rxrepository.dagger2.qualifiers.PerActivity
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
}