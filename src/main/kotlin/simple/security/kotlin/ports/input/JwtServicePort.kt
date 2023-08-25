package simple.security.kotlin.ports.input

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import simple.security.kotlin.application.mapper.TokenMapper
import java.util.function.Function

interface JwtServicePort {

    fun save(tokenMapper: TokenMapper): TokenMapper?

    fun saveTokenUserModel(idUser: Long?, jwtToken: String?): TokenMapper?

    fun revokeAllUserTokens(idUser: Long?)

    fun findAllValidTokenByUser(id: Long?): List<TokenMapper>?

    fun findByToken(token: String?): TokenMapper?

    fun extractUsername(token: String?): String?

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T

    fun generateToken(userDetails: UserDetails?): String?

    fun generateToken(extraClaims: Map<String, Any>?, userDetails: UserDetails?): String?

    fun generateRefreshToken(userDetails: UserDetails?): String?

    fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean
}