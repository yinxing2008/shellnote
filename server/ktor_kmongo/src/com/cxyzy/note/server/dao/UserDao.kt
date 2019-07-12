package com.cxyzy.note.server.dao

import com.cxyzy.note.server.bean.User
import com.cxyzy.note.server.constants.UserConstants.invalidUsn
import org.litote.kmongo.eq

class UserDao : BaseDao() {
    override fun getCollectionName() = "users"
    private fun getCollection() = database.getCollection<User>(getCollectionName())

    suspend fun getUserByLoginId(loginId: String): User? {
        return getCollection().findOne(User::loginId eq loginId)
    }

    suspend fun isExistByLoginId(loginId: String): Boolean {
        return getUserByLoginId(loginId) != null
    }

    suspend fun getUserByUserId(userId: String): User? {
        return getCollection().findOne(User::id eq userId)
    }

    suspend fun increaseUsn(userId: String): Long {
        val user = getUserByUserId(userId)
        var newUsn = invalidUsn
        user?.let {
            user.usn++
            newUsn = user.usn
            update(user.id, user)
        }
        return newUsn
    }

}