package simple.security.kotlin.adapters.outbound

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.model.TokenModel
import simple.security.kotlin.adapters.outbound.repository.TokenRepository
import simple.security.kotlin.application.mapper.TokenMapper
import simple.security.kotlin.ports.output.TokenIntegrationPort

@Component
class TokenIntegrationAdapter : TokenIntegrationPort {

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    override fun save(tokenModel: TokenModel): TokenMapper? =
        Converter.toModel(tokenRepository.save(tokenModel), TokenMapper::class.java)

    override fun saveAll(tokensModel: List<TokenModel>) {
        tokenRepository.saveAll(tokensModel)
    }

    override fun findAllValidTokenByUser(id: Long?): List<TokenMapper>? =
        tokenRepository.findAllValidTokenByUser(id)?.map {
            Converter.toModel(it, TokenMapper::class.java)
        }

    override fun findByToken(token: String?): TokenMapper? =
        tokenRepository.findByToken(token)?.let {
            Converter.toModel(it, TokenMapper::class.java)
        }

}