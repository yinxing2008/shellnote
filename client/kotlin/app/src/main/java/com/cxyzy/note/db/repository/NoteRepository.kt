package com.cxyzy.note.db.repository

import androidx.paging.Config
import androidx.paging.toLiveData
import com.cxyzy.note.db.bean.Note
import com.cxyzy.note.db.dao.NoteDao
import com.cxyzy.note.ext.KoinInject.getFromKoin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class NoteRepository {
    fun list() = getFromKoin<NoteDao>().getNoteList().toLiveData(Config(
            pageSize = 30,
            enablePlaceholders = true))

    suspend fun add(content: String) {
        withContext(Dispatchers.IO) {
            val note = Note(id = UUID.randomUUID().toString(),
                    content = content)
            getFromKoin<NoteDao>().add(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            note.updateTime = Date().time
            getFromKoin<NoteDao>().update(note)
        }
    }

    suspend fun trash(id: String) {
        withContext(Dispatchers.IO) {
            val note = getFromKoin<NoteDao>().getNote(id)
            note?.let {
                note.updateTime = Date().time
                it.isTrash = true
                getFromKoin<NoteDao>().update(it)
            }
        }
    }

    suspend fun del(id: String) {
        withContext(Dispatchers.IO) {
            getFromKoin<NoteDao>().del(id)
        }
    }
}