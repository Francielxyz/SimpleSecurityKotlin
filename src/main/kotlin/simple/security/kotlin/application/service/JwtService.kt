package simple.security.kotlin.application.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import simple.security.kotlin.ports.input.JwtServicePort
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class JwtService : JwtServicePort {
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration30Minutes: Long = 0

    @Value("\${application.security.jwt.refresh-token.expiration}")
    private val refreshExpiration24Hours: Long = 0

    override fun extractUsername(token: String?): String? =
        extractClaim(token, Function { obj: Claims? -> obj?.subject })

    override fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    override fun generateToken(userDetails: UserDetails?, extraClaims: Map<String, Any>?): String? =
        buildToken(userDetails, extraClaims, jwtExpiration30Minutes)

    override fun generateRefreshToken(userDetails: UserDetails?): String? =
        buildToken(userDetails, HashMap(), refreshExpiration24Hours)

    override fun isTokenValid(userDetails: UserDetails?, token: String?): Boolean {
        val username = extractUsername(token)
        return username == userDetails?.username && !isTokenExpired(token)
    }

    private fun buildToken(userDetails: UserDetails?, extraClaims: Map<String, Any>?, expiration: Long): String? =
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