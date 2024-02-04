package simple.security.kotlin.ports.input

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import simple.security.kotlin.application.mapper.AuthenticationMapper
import simple.security.kotlin.application.mapper.UserMapper

interface AuthenticationServicePort {
    fun register(userMapper: UserMapper)

    fun authenticate(email: String?, password: String?): AuthenticationMapper

    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): AuthenticationMapper
}