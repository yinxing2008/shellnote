package com.cxyzy.note.server.dao

import com.cxyzy.note.server.constants.CommonConstants
import com.cxyzy.note.server.ext.inject
import org.litote.kmongo.coroutine.CoroutineClient

abstract class BaseDao {
    private val client: CoroutineClient by inject()
    val database = client.getDatabase(CommonConstants.dbName)


    suspend fun add(obj: List<Any>) {
        database.getCollection<Any>(getCollectionName())
            .insertMany(obj)
    }

    suspend fun add(obj: Any) {
        database.getCollection<Any>(getCollectionName())
            .insertOne(obj)
    }

    suspend fun del(id: Any) {
        database.getCollection<Any>(getCollectionName())
            .deleteOneById(id)
    }

    suspend fun update(id: Any, obj: Any) {
        database.getCollection<Any>(getCollectionName())
            .updateOneById(id, obj)
    }

    abstract fun getCollectionName(): String

}