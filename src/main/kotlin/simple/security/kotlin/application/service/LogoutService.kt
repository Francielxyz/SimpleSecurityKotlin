package simple.security.kotlin.application.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service
import simple.security.kotlin.adapters.outbound.repository.TokenRepository
import simple.security.kotlin.ports.input.JwtServicePort

@Service
@RequiredArgsConstructor
class LogoutService : LogoutHandler {

    @Autowired
    private lateinit var jwtService: JwtServicePort

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        val jwt = authHeader.substring(7)

        jwtService.findByToken(jwt)?.also {
            it.expired = true
            it.revoked = true
            jwtService.save(it)
            SecurityContextHolder.clearContext()
        }
    }
}