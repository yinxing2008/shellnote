package com.cxyzy.note.server.module

import com.cxyzy.note.server.dao.NoteDao
import com.cxyzy.note.server.dao.UserDao
import com.cxyzy.note.server.service.NoteService
import com.cxyzy.note.server.service.UserService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val serviceModule = module {
    single { UserService() }
    single { UserDao() }

    single { NoteService() }
    single { NoteDao() }
}

val dbModule = module {
    single { KMongo.createClient("mongodb://localhost:27017").coroutine }
}
