package com.cxyzy.note.server.routes

import com.cxyzy.note.server.bean.Note
import com.cxyzy.note.server.constants.CommonConstants.BASE_URL
import com.cxyzy.note.server.constants.CommonConstants.maxCountPerRequest
import com.cxyzy.note.server.request.*
import com.cxyzy.note.server.service.NoteService
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.pipeline.PipelineContext
import org.koin.ktor.ext.inject

fun Route.noteRoutes() {

    val noteService: NoteService by inject()

    authenticate {
        route("$BASE_URL/note") {

            post<DownloadNotesReq>("/downloadNotes") { request ->
                var maxCount = maxCountPerRequest
                if (request.maxCount != null && request.maxCount < maxCountPerRequest) {
                    maxCount = request.maxCount
                }
                val result = noteService.getNotes(getUserId(), request.afterUsn, maxCount)
                call.respond(HttpStatusCode.OK, result)
            }

            post<GetNoteReq>("/getNote") { request ->
                val result = noteService.getNote(request.noteId)
                call.respond(HttpStatusCode.OK, result)
            }

            post<DelNoteReq>("/recoverFromTrash") { request ->
                val result = noteService.recoverNoteFromTrash(getUserId(), request.noteId)
                call.respond(HttpStatusCode.OK, result)
            }

            post<DelNoteReq>("/del") { request ->
                val result = noteService.delNote(getUserId(), request.noteId)
                call.respond(HttpStatusCode.OK, result)
            }

            post<TrashNoteReq>("/trash") { request ->
                val result = noteService.trashNote(getUserId(), request.noteId)
                call.respond(HttpStatusCode.OK, result)
            }

            post<AddNoteReq>("/add") { request ->
                val notes = toNoteList(request.list)
                val result = noteService.addNote(getUserId(), notes)

                call.respond(HttpStatusCode.OK, result)
            }

            post<UpdateNoteReq>("/update") { request ->
                val notes = toNoteList(request.list)
                val result = noteService.updateNote(getUserId(), notes)
                call.respond(HttpStatusCode.OK, result)
            }

            post<BatchUpdatedNoteReq>("/batchUpdate") { request ->
                val toAddNotes = toNoteList(request.toAddList)
                val toUpdateNotes = toNoteList(request.toUpdateList)
                val result = noteService.batchUpdate(getUserId(), toAddNotes, toUpdateNotes)
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }

}

private fun PipelineContext<Unit, ApplicationCall>.toNoteList(reqList: List<SimpleNoteReq>): MutableList<Note> {
    val toUpdateNotes = mutableListOf<Note>()
    for (simpleNoteReq in reqList) {
        val note = wrapAsNote(simpleNoteReq)
        toUpdateNotes.add(note)
    }
    return toUpdateNotes
}

private fun PipelineContext<Unit, ApplicationCall>.wrapAsNote(simpleNoteReq: SimpleNoteReq) =
    Note(
        id = simpleNoteReq.noteId,
        userId = getUserId(),
        title = simpleNoteReq.title,
        content = simpleNoteReq.content
    )



