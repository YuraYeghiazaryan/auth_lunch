package com.sovats.auth.service

import com.sovats.auth.api.LoginRequest
import com.sovats.auth.api.SignupRequest
import com.sovats.auth.persistence.entity.User
import com.sovats.auth.persistence.repository.UserRepository
import com.sovats.auth.security.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun signup(req: SignupRequest): User {
        if (userRepository.existsByEmail(req.email)) {
            throw IllegalStateException("User with email ${req.email} already exists")
        }
        val user = User(
            email = req.email,
            password = passwordEncoder.encode(req.password),
            firstName = req.firstName,
            lastName = req.lastName
        )
        return userRepository.save(user)
    }

    fun login(req: LoginRequest): String {
        val user = userRepository.findByEmail(req.email)
            ?: throw IllegalStateException("Invalid credentials")

        if (!passwordEncoder.matches(req.password, user.password)) {
            throw IllegalStateException("Invalid credentials")
        }

        val claims = mapOf(
            "email" to user.email,
            "firstName" to user.firstName
        )

        return jwtUtil.generateToken(user.id.toString(), claims)
    }
}
