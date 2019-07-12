package com.cxyzy.note.server.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authentication
import io.ktor.util.pipeline.PipelineContext

fun PipelineContext<Unit, ApplicationCall>.getUserId(): String {
    val user = call.authentication.principal<UserIdPrincipal>()
    return user!!.name
}

