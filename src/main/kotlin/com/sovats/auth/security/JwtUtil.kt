package com.sovats.auth.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Component
class JwtUtil(
    jwtProperties: JwtProperties
) {
    private val key: Key = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    private val issuer: String = jwtProperties.issuer
    private val ttlMinutes: Long = jwtProperties.accessTokenTtlMinutes

    fun generateToken(subject: String, additionalClaims: Map<String, Any> = emptyMap()): String {
        val now = Instant.now()
        val exp = now.plus(ttlMinutes, ChronoUnit.MINUTES)

        val b = Jwts.builder()
            .setIssuer(issuer)
            .setSubject(subject)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(exp))
            .addClaims(additionalClaims)
            .signWith(key, SignatureAlgorithm.HS256)

        return b.compact()
    }

    fun validateAndGetSubject(token: String): String {
        val parsed = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        return parsed.body.subject
    }
}
