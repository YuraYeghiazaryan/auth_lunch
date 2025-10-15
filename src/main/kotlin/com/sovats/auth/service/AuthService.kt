package com.sovats.auth.service

import com.sovats.auth.api.LoginRequest
import com.sovats.auth.api.SignupRequest
import com.sovats.auth.persistence.entity.User
import com.sovats.auth.persistence.repository.UserRepository
import com.sovats.auth.security.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun signup(req: SignupRequest): User {
        if (userRepository.existsByEmail(req.email)) {
            throw IllegalStateException("User with email ${req.email} already exists")
        }
        val user = User(
            email = req.email,
            password = req.password,
            firstName = req.firstName,
            lastName = req.lastName
        )
        return userRepository.save(user)
    }

    fun login(req: LoginRequest): String {
        val user = userRepository.findByEmail(req.email)
            ?: throw IllegalStateException("Invalid credentials")

        if (req.password != user.password) {
            throw IllegalStateException("Invalid credentials")
        }

        // additionalClaims can contain roles etc
        val token = jwtUtil.generateToken(user.id.toString())
        return token
    }
}
