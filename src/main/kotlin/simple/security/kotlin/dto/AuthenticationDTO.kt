package simple.security.kotlin.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationDTO(
    @JsonProperty("access_token")
    var accessToken: String? = null,

    @JsonProperty("refresh_token")
    var refreshToken: String? = null
)