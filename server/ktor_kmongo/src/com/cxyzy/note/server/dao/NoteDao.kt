package com.cxyzy.note.server.dao

import com.cxyzy.note.server.bean.Note
import org.litote.kmongo.ascending
import org.litote.kmongo.eq
import org.litote.kmongo.gt

class NoteDao : BaseDao() {
    override fun getCollectionName() = "note"
    private fun getCollection() = database.getCollection<Note>(getCollectionName())

    suspend fun getNote(noteId: String): Note? {
        return getCollection().findOne(Note::id eq noteId)
    }

    suspend fun getNotes(userId: String, afterUsn: Long, maxCount: Int): List<Note> {
        return getCollection()
            .find(Note::userId eq userId, Note::usn gt afterUsn)
            .sort(ascending(Note::usn))
            .limit(maxCount).toList()
    }
}