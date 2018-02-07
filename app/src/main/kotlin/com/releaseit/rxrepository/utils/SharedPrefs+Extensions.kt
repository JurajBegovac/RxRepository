package com.releaseit.rxrepository.utils

import android.content.SharedPreferences

/**
 * Created by jurajbegovac on 02/02/2018.
 */
const val PACKAGE = "com.releseit.rxrepository"
const val PREF_NAME = "$PACKAGE.prefs"
const val KEY = "$PREF_NAME.key"
const val KEY_LOCAL_DB = "$KEY.localdb"

enum class LocalDb {
    SQLITE, REALM
}

fun SharedPreferences.getLocalDb() = LocalDb.valueOf(getString(KEY_LOCAL_DB, LocalDb.SQLITE.name))
fun SharedPreferences.saveLocalDb(localDb: LocalDb) = edit().putString(KEY_LOCAL_DB, localDb.name).commit()
