package simple.security.kotlin.ports.input

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.util.function.Function

interface JwtServicePort {
    fun extractUsername(token: String?): String?

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T

    fun generateToken(userDetails: UserDetails?): String?

    fun generateToken(extraClaims: Map<String, Any>?, userDetails: UserDetails?): String?

    fun generateRefreshToken(userDetails: UserDetails?): String?

    fun isTokenValid(token: String?, userDetails: UserDetails?): Boolean
}