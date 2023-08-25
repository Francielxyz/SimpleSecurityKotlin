package simple.security.kotlin.adapters.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import simple.security.kotlin.application.service.JwtService

@Component
@RequiredArgsConstructor
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain) {
        if (request.servletPath?.contains("/api/v1/auth") == true) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(jwt)

        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(userEmail)

            val isTokenValid = jwtService.findByToken(jwt).let { tokenModel ->
                tokenModel?.expired == true && tokenModel.revoked == true
            }

            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails!!.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
    }
//
//    val authHeader = request.getHeader("Authorization").also {
//        if (it == null || !it.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response)
//            return
//        }
//    }
//
//    val jwt = authHeader.substring(7)
//
//    jwtService.extractUsername(jwt)?.let {
//        if (it.isNotBlank() && SecurityContextHolder.getContext().authentication == null) {
//
//            val isTokenValid: Boolean? = tokenRepository.findByToken(jwt).map { tokenModel ->
//                tokenModel?.expired == true && tokenModel.revoked == true
//            }?.orElse(false)
//
//            userDetailsService.loadUserByUsername(it).let { userDetails ->
//                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid == true) {
//                    val authToken = UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.authorities
//                    )
//
//                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
//
//                    SecurityContextHolder.getContext().authentication = authToken
//                }
//            }
//        }
//    }
}