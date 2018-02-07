package com.releaseit.rxrepository.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Process

/**
 * Copyright (C) 2014 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class ProcessPhoenix : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intents: ArrayList<Intent> = intent.getParcelableArrayListExtra(KEY_RESTART_INTENTS)
        startActivities(intents.toTypedArray())
        finish()
        Runtime.getRuntime().exit(0) // Kill kill kill!
    }

    companion object {
        private const val KEY_RESTART_INTENTS = "phoenix_restart_intents"

        /**
         * Call to restart the application process using the specified intents.
         * <p>
         * Behavior of the current process after invoking this method is undefined.
         */
        /**
         * Call to restart the application process using the [default][Intent.CATEGORY_DEFAULT]
         * activity as an intent.
         *
         *
         * Behavior of the current process after invoking this method is undefined.
         */

        /**
         *
         */
        @JvmOverloads
        fun triggerRebirth(context: Context, vararg nextIntents: Intent = arrayOf(getRestartIntent(context))) {
            val intent = Intent(context, ProcessPhoenix::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK) // In case we are called with non-Activity context.
            intent.putParcelableArrayListExtra(KEY_RESTART_INTENTS, ArrayList(nextIntents.toList()))
            context.startActivity(intent)
            if (context is Activity) {
                context.finish()
            }
            Runtime.getRuntime().exit(0) // Kill kill kill!
        }

        private fun getRestartIntent(context: Context): Intent {
            val packageName = context.packageName
            val defaultIntent = context.packageManager.getLaunchIntentForPackage(packageName)
            if (defaultIntent != null) {
                defaultIntent.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                return defaultIntent
            }

            throw IllegalStateException("Unable to determine default activity for "
                    + packageName
                    + ". Does an activity specify the DEFAULT category in its intent filter?")
        }

        /**
         * Checks if the current process is a temporary Phoenix Process.
         * This can be used to avoid initialisation of unused resources or to prevent running code that
         * is not multi-process ready.
         *
         * @return true if the current process is a temporary Phoenix Process
         */
        fun isPhoenixProcess(context: Context): Boolean {
            val currentPid = Process.myPid()
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningProcesses = manager.runningAppProcesses
            if (runningProcesses != null) {
                for (processInfo in runningProcesses) {
                    if (processInfo.pid == currentPid && processInfo.processName.endsWith(":phoenix")) {
                        return true
                    }
                }
            }
            return false
        }
    }
}
