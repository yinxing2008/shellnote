package com.cxyzy.note.server.routes

import com.cxyzy.note.server.bean.User
import com.cxyzy.note.server.constants.CommonConstants
import com.cxyzy.note.server.constants.CommonConstants.BASE_URL
import com.cxyzy.note.server.request.LoginReq
import com.cxyzy.note.server.request.RegisterReq
import com.cxyzy.note.server.service.UserService
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Route.userRoutes() {

    val userService: UserService by inject()

    route("$BASE_URL/user") {

        post<RegisterReq>("/register") { request ->
            val user = User(
                loginId = request.loginId,
                password = request.password
            )
            val result = userService.register(user)
            call.respond(HttpStatusCode.OK, result)
        }

        post<LoginReq>("/login") { request ->
            val result = userService.login(request.loginId, request.password)
            call.respond(HttpStatusCode.OK, result)
        }
    }

    authenticate {
        route("$BASE_URL/user") {
            get("/checkSyncState") {
                val result = userService.checkSyncState(getUserId())
                call.respond(HttpStatusCode.OK, result)
            }
        }
    }
}





