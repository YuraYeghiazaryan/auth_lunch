package com.sovats.auth.transport

import com.sovats.auth.api.AuthResponse
import com.sovats.auth.api.LoginRequest
import com.sovats.auth.api.SignupRequest
import com.sovats.auth.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtil: com.sovats.auth.security.JwtUtil,
    private val jwtProps: com.sovats.auth.security.JwtProperties
) {

    @PostMapping("/signup")
    fun signup(@RequestBody req: SignupRequest): ResponseEntity<Any> {
        val created = authService.signup(req)
        return ResponseEntity.status(201).body(mapOf("id" to created.id, "email" to created.email))
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
        val token = authService.login(req)
        val expiresIn = jwtProps.accessTokenTtlMinutes * 60
        return ResponseEntity.ok(AuthResponse(accessToken = token, expiresInSeconds = expiresIn))
    }
}
