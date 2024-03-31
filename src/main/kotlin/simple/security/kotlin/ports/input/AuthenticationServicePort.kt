package simple.security.kotlin.ports.input

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import simple.security.kotlin.application.mapper.AuthenticationMapper

interface AuthenticationServicePort {
    fun login(email: String?, password: String?): AuthenticationMapper

    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): AuthenticationMapper

}