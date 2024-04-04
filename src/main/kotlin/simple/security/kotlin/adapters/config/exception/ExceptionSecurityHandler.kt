package simple.security.kotlin.adapters.config.exception

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class ExceptionSecurityHandler(private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) {
        val message = authException?.message ?: "You need to be authenticated to access this feature"
        val statusCode = HttpStatus.UNAUTHORIZED

        val jsonResponseBody = objectMapper.writeValueAsString(mapOf(
            "message" to message,
            "statusCode" to statusCode
        ))

        response.contentType = "application/json"
        response.status = statusCode.value()
        response.writer.write(jsonResponseBody)
    }
}