package com.cxyzy.note.utils.spUtils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.cxyzy.note.App
import com.cxyzy.note.PrefKeys
import com.cxyzy.note.PrefKeys.USER_ID_BEFORE_LOGIN
import com.cxyzy.note.SHARED_SP_FILE

abstract class BaseSPUtil {

    fun getFromSP(key: String, default: String? = null): String? = getSPFile().getString(key, default)
    fun saveInSP(key: String, value: String) = getSPFile().edit().putString(key, value).apply()

    fun getFromSP(key: String, default: Long): Long = getSPFile().getLong(key, default)
    fun saveInSP(key: String, value: Long) = getSPFile().edit().putLong(key, value).apply()
    fun delInSP(key: String) = getSPFile().edit().remove(key).apply()

    fun getUserIdFromSP() = getFromSP(getSharedSPFile(), PrefKeys.USER_ID) ?: USER_ID_BEFORE_LOGIN
    fun saveUserIdInSP(userId: String) = saveInSP(getSharedSPFile(), PrefKeys.USER_ID, userId)
    fun delUserIdInSP() = delInSP(getSharedSPFile(), PrefKeys.USER_ID)

    private fun getFromSP(sp: SharedPreferences = getSPFile(), key: String, default: String? = null): String? = sp.getString(key, default)
    private fun saveInSP(sp: SharedPreferences = getSPFile(), key: String, value: String) = sp.edit().putString(key, value).apply()
    private fun delInSP(sp: SharedPreferences = getSPFile(), key: String) = sp.edit().remove(key).apply()

    private fun getSharedSPFile(): SharedPreferences = App.context.getSharedPreferences(SHARED_SP_FILE, MODE_PRIVATE)

    abstract fun getSPFile(): SharedPreferences

}