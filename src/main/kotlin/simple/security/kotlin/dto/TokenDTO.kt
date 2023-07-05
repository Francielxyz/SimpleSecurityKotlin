package simple.security.kotlin.dto

data class TokenDTO(
    var token: String? = null,
    var refreshToken: String? = null,
)