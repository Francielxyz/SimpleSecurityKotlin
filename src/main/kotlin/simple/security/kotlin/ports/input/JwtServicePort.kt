package simple.security.kotlin.ports.input

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.util.function.Function

interface JwtServicePort {
    fun extractUsername(token: String?): String?

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T

    fun generateToken(userDetails: UserDetails?, extraClaims: Map<String, Any>?): String?

    fun generateRefreshToken(userDetails: UserDetails?): String?

    fun isTokenValid(userDetails: UserDetails?, token: String?): Boolean
}