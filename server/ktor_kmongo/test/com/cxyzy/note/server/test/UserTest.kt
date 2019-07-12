package com.cxyzy.note.server.test

import com.cxyzy.note.server.constants.CommonConstants.BASE_URL
import com.cxyzy.note.server.constants.CommonConstants.SUCCESS
import com.cxyzy.note.server.constants.ErrorInfo.USER_EXISTS
import com.cxyzy.note.server.module
import com.cxyzy.note.server.request.LoginReq
import com.cxyzy.note.server.request.RegisterReq
import com.cxyzy.note.server.response.BaseResp
import com.cxyzy.note.server.response.EmptyResp
import com.cxyzy.note.server.response.LoginResp
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.litote.kmongo.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun testRegister() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "$BASE_URL/user/register")
            {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                val registerReq = RegisterReq(loginId, password)
                setBody(registerReq.json)
            }.apply {
                val result = ObjectMapper().readValue<BaseResp<EmptyResp>>(response.content!!)
                val isSuccess = (result.code == SUCCESS || result.code == USER_EXISTS)
                assertTrue(isSuccess)
            }
        }
    }

    @Test
    fun testLogin() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "$BASE_URL/user/login")
            {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                val loginReq = LoginReq(loginId, password)
                setBody(loginReq.json)
            }.apply {

                val result = ObjectMapper().readValue<BaseResp<LoginResp>>(response.content!!)
                assertEquals(0, result.code)
            }
        }
    }

}