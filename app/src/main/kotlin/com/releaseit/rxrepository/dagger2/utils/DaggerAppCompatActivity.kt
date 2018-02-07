package com.releaseit.rxrepository.dagger2.utils

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import javax.inject.Inject

/**
 * Created by jurajbegovac on 02/02/2018.
 */
abstract class DaggerAppCompatActivity : AppCompatActivity(), HasFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun fragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }
}