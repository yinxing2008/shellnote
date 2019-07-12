package com.cxyzy.note.server.test

import com.cxyzy.note.server.bean.Note
import com.cxyzy.note.server.constants.CommonConstants.BASE_URL
import com.cxyzy.note.server.module
import com.cxyzy.note.server.request.*
import com.cxyzy.note.server.response.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.litote.kmongo.json
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NoteTest {
    private var token: String? = null

    @Test
    fun testAddNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            handleRequest(HttpMethod.Post, "$BASE_URL/note/add")
            {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Authorization", "Bearer $token")

                val list = mutableListOf<SimpleNoteReq>()
                for (i in 1..10) {
                    list.add(SimpleNoteReq(UUID.randomUUID().toString(), "title$i", "content$i"))
                }
                val addNoteReq = AddNoteReq(list)
                setBody(addNoteReq.json)
            }.apply {
                val result = ObjectMapper().readValue<BaseResp<AddNoteResp>>(response.content!!)
                assertEquals(0, result.code)
            }
        }
    }

    @Test
    fun testGetNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            val noteId = addNote()
            val note = getNote(noteId)
            assertEquals(note?.id, noteId)
        }
    }

    @Test
    fun testDelNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            val noteId = addNote()
            val note = getNote(noteId)
            assertFalse(note?.isDeleted!!)
            assertEquals(note.id, noteId)
            delNote(noteId)
            val note1 = getNote(noteId)
            assertTrue(note1?.isDeleted!!)
        }
    }

    @Test
    fun testTrashNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            val noteId = addNote()
            val note = getNote(noteId)
            assertFalse(note?.isTrashed!!)
            assertEquals(note.id, noteId)
            trashNote(noteId)
            val note1 = getNote(noteId)
            assertTrue(note1?.isTrashed!!)
        }
    }

    @Test
    fun testUpdateNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            val noteId = addNote()
            val newTitle = "newTitle"
            val newContent = "newContent"
            updateNote(noteId, newTitle, newContent)
            val note = getNote(noteId)
            assertEquals(newTitle, note?.title)
            assertEquals(newContent, note?.content)
        }
    }


    @Test
    fun testListNote() {
        withTestApplication({ module(testing = true) }) {
            loginToGetToken()
            handleRequest(HttpMethod.Post, "$BASE_URL/note/downloadNotes")
            {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Authorization", "Bearer $token")
                val getNoteReq = DownloadNotesReq(0)
                setBody(getNoteReq.json)
            }.apply {

                val result = ObjectMapper().readValue<BaseResp<DownloadNotesResp>>(response.content!!)
                assertEquals(0, result.code)
            }
        }
    }


    private fun TestApplicationEngine.getNote(noteId: String): Note? {
        var note: Note?

        handleRequest(HttpMethod.Post, "$BASE_URL/note/getNote")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $token")
            val getNoteReq = GetNoteReq(noteId)
            setBody(getNoteReq.json)
        }.apply {
            val result = ObjectMapper().readValue<BaseResp<GetNoteResp>>(response.content!!)
            note = result.data?.note
        }
        return note
    }

    private fun TestApplicationEngine.delNote(noteId: String) {
        handleRequest(HttpMethod.Post, "$BASE_URL/note/del")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $token")
            val getNoteReq = DelNoteReq(noteId)
            setBody(getNoteReq.json)
        }.apply {
            ObjectMapper().readValue<BaseResp<AddNoteResp>>(response.content!!)
        }
    }

    private fun TestApplicationEngine.trashNote(noteId: String) {
        handleRequest(HttpMethod.Post, "$BASE_URL/note/trash")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $token")
            val getNoteReq = TrashNoteReq(noteId)
            setBody(getNoteReq.json)
        }.apply {
            ObjectMapper().readValue<BaseResp<AddNoteResp>>(response.content!!)
        }
    }

    private fun TestApplicationEngine.addNote(): String {
        var noteId: String
        handleRequest(HttpMethod.Post, "$BASE_URL/note/add")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $token")
            val list = mutableListOf(SimpleNoteReq(UUID.randomUUID().toString(), "title1", "content1"))
            val addNoteReq = AddNoteReq(list)
            setBody(addNoteReq.json)
        }.apply {
            val result = ObjectMapper().readValue<BaseResp<AddNoteResp>>(response.content!!)
            noteId = result.data?.notes?.get(0)?.id!!
        }
        return noteId
    }

    private fun TestApplicationEngine.updateNote(noteId: String, newTitle: String, newContent: String) {
        handleRequest(HttpMethod.Post, "$BASE_URL/note/update")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Authorization", "Bearer $token")

            val list = mutableListOf(SimpleNoteReq(noteId, newTitle, newContent))
            val updateNoteReq = UpdateNoteReq(list)
            setBody(updateNoteReq.json)
        }.apply {
            ObjectMapper().readValue<BaseResp<AddNoteResp>>(response.content!!)
        }
    }

    private fun TestApplicationEngine.loginToGetToken() {
        if (token != null) return
        handleRequest(HttpMethod.Post, "$BASE_URL/user/login")
        {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())

            val loginReq = LoginReq(loginId, password)
            setBody(loginReq.json)
        }.apply {
            val result = ObjectMapper().readValue<BaseResp<LoginResp>>(response.content!!)
            token = result.data?.token!!
        }
    }

}