package com.cxyzy.note.server

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.cxyzy.note.server.constants.CommonConstants.TOKEN_VALID_SECONDS
import java.util.*

object Auth {
    private const val SECRET_KEY = "secret"
    private val algorithm = Algorithm.HMAC512(SECRET_KEY)
    private const val issuer = "ktor.io"

    fun makeJwtVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun makeToken(loginId: String): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("loginId", loginId)
        .withExpiresAt(getExpiration())
        .sign(algorithm)

    private fun getExpiration() = Date(System.currentTimeMillis() + TOKEN_VALID_SECONDS * 1000)

}