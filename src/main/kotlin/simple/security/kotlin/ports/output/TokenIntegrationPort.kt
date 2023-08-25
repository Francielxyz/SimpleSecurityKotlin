package simple.security.kotlin.ports.output

import simple.security.kotlin.adapters.model.TokenModel
import simple.security.kotlin.application.mapper.TokenMapper

interface TokenIntegrationPort {
    fun save(tokenModel: TokenModel): TokenMapper?

    fun saveAll(tokensModel: List<TokenModel>)

    fun findAllValidTokenByUser(id: Long?): List<TokenMapper>?

    fun findByToken(token: String?): TokenMapper?
}