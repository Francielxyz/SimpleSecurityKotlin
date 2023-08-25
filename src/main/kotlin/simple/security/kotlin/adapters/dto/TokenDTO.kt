package simple.security.kotlin.adapters.dto

import simple.security.kotlin.adapters.enums.TokenType
import simple.security.kotlin.adapters.model.UserModel

data class TokenDTO(
    var id: Long? = null,
    var token: String? = null,
    var tokenType: TokenType? = null,
    var revoked: Boolean? = null,
    var expired: Boolean? = null,
    var user: UserModel? = null,
)