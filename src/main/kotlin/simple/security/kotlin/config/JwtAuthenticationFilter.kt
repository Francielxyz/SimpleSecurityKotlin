//package simple.security.kotlin.config
//
//import jakarta.servlet.FilterChain
//import jakarta.servlet.ServletException
//import jakarta.servlet.http.HttpServletRequest
//import jakarta.servlet.http.HttpServletResponse
//import lombok.RequiredArgsConstructor
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.lang.NonNull
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
//import org.springframework.stereotype.Component
//import org.springframework.web.filter.OncePerRequestFilter
//import simple.security.kotlin.repository.TokenRepository
//import simple.security.kotlin.service.JwtService
//import java.io.IOException
//
//@Component
//@RequiredArgsConstructor
//class JwtAuthenticationFilter : OncePerRequestFilter() {
//
//    @Autowired
//    private lateinit var jwtService: JwtService
//
//    @Autowired
//    private lateinit var userDetailsService: UserDetailsService
//
//    @Autowired
//    private lateinit var tokenRepository: TokenRepository
//
//    @Throws(ServletException::class, IOException::class)
//    override fun doFilterInternal(
//        @NonNull request: HttpServletRequest,
//        @NonNull response: HttpServletResponse,
//        @NonNull filterChain: FilterChain
//    ) {
//        val authHeader = request.getHeader("Authorization").also {
//            if (it == null || it.startsWith("Bearer ")) {
//                filterChain.doFilter(request, response)
//                return
//            }
//        }
//
//        val jwt = authHeader.substring(7)
//
//        jwtService.extractUsername(jwt).let {
//            if (it.isNotBlank() && SecurityContextHolder.getContext().authentication == null) {
//
//                val isTokenValid: Boolean = tokenRepository.findByToken(jwt).map { tokenModel ->
//                    tokenModel?.expired == true && tokenModel.revoked == true
//                }?.orElse(false)
//
//                userDetailsService.loadUserByUsername(it).let { userDetails ->
//                    if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
//                        val authToken = UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.authorities
//                        )
//
//                        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
//
//                        SecurityContextHolder.getContext().authentication = authToken
//                    }
//                }
//            }
//        }
//    }
//}
