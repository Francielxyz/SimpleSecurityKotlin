package simple.security.kotlin.application.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.enums.TokenType
import simple.security.kotlin.adapters.model.TokenModel
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.mapper.TokenMapper
import simple.security.kotlin.ports.input.JwtServicePort
import simple.security.kotlin.ports.output.TokenIntegrationPort
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService : JwtServicePort {

    @Autowired
    private lateinit var tokenIntegrationPort: TokenIntegrationPort

    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration: Long = 0

    @Value("\${application.security.jwt.refresh-token.expiration}")
    private val refreshExpiration: Long = 0

    @Transactional(rollbackFor = [Throwable::class])
    override fun save(tokenMapper: TokenMapper): TokenMapper? =
        tokenIntegrationPort.save(Converter.toModel(tokenMapper, TokenModel::class.java))

    override fun saveTokenUserModel(idUser: Long?, jwtToken: String?): TokenMapper? {
        val tokenMapper = TokenMapper(
            token = jwtToken,
            tokenType = TokenType.BEARER,
            revoked = false,
            expired = false,
            user = UserModel(id = idUser)
        )

        return save(tokenMapper)
    }


    @Transactional(rollbackFor = [Throwable::class])
    override fun revokeAllUserTokens(idUser: Long?) {
        tokenIntegrationPort.findAllValidTokenByUser(idUser)?.also { token ->
            token.forEach {
                it.expired = true
                it.revoked = true
            }

            tokenIntegrationPort.saveAll(token.map {
                Converter.toModel(it, TokenModel::class.java)
            })
        }
    }


    override fun findAllValidTokenByUser(id: Long?): List<TokenMapper>? =
        tokenIntegrationPort.findAllValidTokenByUser(id)

    override fun findByToken(token: String?): TokenMapper? =
        tokenIntegrationPort.findByToken(token)

    override fun extractUsername(token: String?): String? =
        extractClaim(token, Function { obj: Claims? -> obj?.subject })

    override fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    override fun generateToken(userDetails: UserDetails?): String? =
        generateToken(HashMap<String, Any>(), userDetails)

    override fun generateToken(extraClaims: Map<String, Any>?, userDetails: UserDetails?): String? =
        buildToken(extraClaims, userDetails, jwtExpiration)

    override fun generateRefreshToken(userDetails: UserDetails?): String? =
        buildToken(HashMap(), userDetails, refreshExpiration)

    override fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean {
        val username = extractUsername(token)
        return username == userDetails?.username && !isTokenExpired(token)
    }

    private fun buildToken(extraClaims: Map<String, Any>?, userDetails: UserDetails?, expiration: Long): String? =
        Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails?.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()

    private fun isTokenExpired(token: String?): Boolean =
        extractExpiration(token).before(Date())

    private fun extractExpiration(token: String?): Date =
        extractClaim(token) { obj: Claims -> obj.expiration }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSignInKey(): Key =
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
}