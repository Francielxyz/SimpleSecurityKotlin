package simple.security.kotlin.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service
import simple.security.kotlin.repository.TokenRepository

@Service
@RequiredArgsConstructor
class LogoutService : LogoutHandler {

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    override fun logout(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        val authHeader = request.getHeader("Authorization").also {
            if (it == null || !it.startsWith("Bearer ")) return
        }

        tokenRepository.findByToken(authHeader?.substring(7)).orElse(null)?.also {
            it.expired = true
            it.revoked = true
            tokenRepository.save(it)

            SecurityContextHolder.clearContext()
        }
    }
}