package simple.security.kotlin.application.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class LogoutService : LogoutHandler {

    override fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }
        SecurityContextHolder.clearContext()
    }
}