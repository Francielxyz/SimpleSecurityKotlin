package simple.security.kotlin.application.mapper

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationMapper(
    @JsonProperty("access_token")
    var accessToken: String? = null,

    @JsonProperty("refresh_token")
    var refreshToken: String? = null
)