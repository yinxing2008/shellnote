package com.cxyzy.note.utils.spUtils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.cxyzy.note.App
import com.cxyzy.note.PrefKeys
import com.cxyzy.note.PrefKeys.USER_ID_BEFORE_LOGIN
import com.cxyzy.note.USER_SP_FILE_PREFIX
import com.cxyzy.note.network.response.LoginResp
import com.google.gson.Gson
import java.util.*

object UserSPUtil : BaseSPUtil() {
    private val gson = Gson()

    fun getLoginRespFromSP(): LoginResp? {
        var value = getFromSP(PrefKeys.LOGIN_RESP)
        value?.let {
            return gson.fromJson(it, LoginResp::class.java)
        }
        return null
    }

    fun saveLoginRespInSP(loginId: LoginResp) {
        checkUserIdInSP()
        saveInSP(PrefKeys.LOGIN_RESP, gson.toJson(loginId))
        saveInSP(PrefKeys.LOGIN_TIME, Date().time)
    }

    fun delLoginRespInSP() = delInSP(PrefKeys.LOGIN_RESP)

    fun getLoginTimeFromSP() = getFromSP(PrefKeys.LOGIN_TIME, 0L)

    fun getLoginIdFromSP() = getFromSP(PrefKeys.LOGIN_ID)
    fun saveLoginIdInSP(loginId: String) {
        checkUserIdInSP()
        saveInSP(PrefKeys.LOGIN_ID, loginId)
    }

    fun delLoginIdInSP() = delInSP(PrefKeys.LOGIN_ID)


    fun getLoginPassFromSP() = getFromSP(PrefKeys.LOGIN_PASS)
    fun saveLoginPassInSP(loginPass: String) {
        checkUserIdInSP()
        saveInSP(PrefKeys.LOGIN_PASS, loginPass)
    }

    fun delLoginPassInSP() = delInSP(PrefKeys.LOGIN_PASS)

    private fun checkUserIdInSP() {
        if (getUserIdFromSP() == USER_ID_BEFORE_LOGIN) {
            throw Exception("Please call saveUserIdInSP first.")
        }
    }

    fun getServerFullSyncBeforeFromSP() = getFromSP(PrefKeys.SERVER_FULL_SYNC_BEFORE, 0L)
    fun saveServerFullSyncBeforeInSP(serverFullSyncBefore: Long) = saveInSP(PrefKeys.SERVER_FULL_SYNC_BEFORE, serverFullSyncBefore)

    fun getLocalLastFullSyncDateFromSP() = getFromSP(PrefKeys.LOCAL_LAST_FULL_SYNC_DATE, 0L)
    fun saveLocalLastFullSyncDateInSP(localLastFullSyncDate: Long) {
        saveInSP(PrefKeys.LOCAL_LAST_FULL_SYNC_DATE, localLastFullSyncDate)
    }

    fun getMaxLocalNoteUsnFromSP() = getFromSP(PrefKeys.MAX_LOCAL_NOTE_USN, 0L)
    fun resetMaxNoteUsnInSP() {
        saveMaxNoteUsnInSP(0)
    }

    fun saveMaxNoteUsnInSP(usn: Long) {
        saveInSP(PrefKeys.MAX_LOCAL_NOTE_USN, usn)
    }

    override fun getSPFile(): SharedPreferences = App.context.getSharedPreferences(USER_SP_FILE_PREFIX + getUserIdFromSP(), MODE_PRIVATE)
}