package com.sovats.auth.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "auth.jwt")
data class JwtProperties(
    var secret: String = "",
    var issuer: String = "",
    var accessTokenTtlMinutes: Long = 60
)
