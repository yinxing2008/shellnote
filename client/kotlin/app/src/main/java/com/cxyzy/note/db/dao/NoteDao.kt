package com.cxyzy.note.db.dao

import androidx.paging.DataSource
import androidx.room.*
import com.cxyzy.note.DB_FIELD_FALSE
import com.cxyzy.note.DB_FIELD_TRUE
import com.cxyzy.note.db.bean.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE isTrash = $DB_FIELD_FALSE ORDER BY updateTime DESC")
    fun getNoteList(): DataSource.Factory<Int, Note>

    @Query("SELECT * FROM Note WHERE isDirty = $DB_FIELD_TRUE AND usn IS NULL ORDER BY updateTime ASC LIMIT :limitPerQuery")
    fun getDirtyNotesWithoutUsn(limitPerQuery: Int): List<Note>

    @Query("SELECT * FROM Note WHERE isDirty = $DB_FIELD_TRUE AND usn IS NOT NULL ORDER BY updateTime ASC LIMIT :limitPerQuery")
    fun getDirtyNotesWithUsn(limitPerQuery: Int): List<Note>

    @Query("SELECT * FROM Note WHERE id=:id")
    fun getNote(id: String): Note?

    @Insert
    fun add(note: Note)

    @Insert
    fun add(notes: List<Note>)

    @Update
    fun update(note: Note)

    @Update
    fun update(notes: List<Note>)

    @Delete
    fun del(note: Note)

    @Query("DELETE FROM Note WHERE id = :id")
    fun del(id: String)

    @Query("DELETE FROM Note WHERE isDirty = $DB_FIELD_FALSE")
    fun delUnDirtyNotes()

    @Query("UPDATE Note set isDirty = $DB_FIELD_FALSE where id=:id")
    fun removeDirtyFlag(id: String)

}