package simple.security.kotlin.application.mapper

import simple.security.kotlin.adapters.enums.TokenType
import simple.security.kotlin.adapters.model.UserModel

data class TokenMapper(
    var id: Long? = null,
    var token: String? = null,
    var tokenType: TokenType? = null,
    var revoked: Boolean? = null,
    var expired: Boolean? = null,
    var user: UserModel? = null,
)