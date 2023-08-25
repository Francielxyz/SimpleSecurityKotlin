package simple.security.kotlin.application.service

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.AuthenticationDTO
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.mapper.AuthenticationMapper
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.AuthenticationServicePort
import simple.security.kotlin.ports.input.JwtServicePort
import simple.security.kotlin.ports.output.AuthenticationIntegrationPort
import java.io.IOException

@Service
@RequiredArgsConstructor
class AuthenticationService : AuthenticationServicePort {

    @Autowired
    private lateinit var authenticationIntegration: AuthenticationIntegrationPort

    @Autowired
    private lateinit var jwtService: JwtServicePort

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Transactional(rollbackFor = [Throwable::class])
    override fun register(userMapper: UserMapper) {
        userMapper.password = passwordEncoder.encode(userMapper.password)
        userMapper.role = Role.USER

        val userModel = authenticationIntegration.save(
            Converter.toModel(userMapper, UserModel::class.java)
        ).let {
            Converter.toModel(it, UserModel::class.java)
        }

        jwtService.saveTokenUserModel(userModel?.id, jwtService.generateToken(userModel))
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun authenticate(email: String?, password: String?): AuthenticationMapper {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                email,
                password
            )
        )

        val userModel = authenticationIntegration.findByEmail(email)?.let {
            Converter.toModel(it, UserModel::class.java)
        }

        val jwtToken = jwtService.generateToken(userModel)
        val refreshToken = jwtService.generateRefreshToken(userModel)

        jwtService.revokeAllUserTokens(userModel?.id)
        jwtService.saveTokenUserModel(userModel?.id, jwtToken)

        val authenticationMapper = AuthenticationMapper().also {
            it.accessToken = jwtToken
            it.refreshToken = refreshToken
        }

        return authenticationMapper
    }

    @Throws(IOException::class)
    override fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        val refreshToken = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(refreshToken)

        if (userEmail != null) {
            val user = authenticationIntegration.findByEmail(userEmail)?.let {
                Converter.toModel(it, UserModel::class.java)
            }

            if (jwtService.isTokenValid(refreshToken, user)) {
                val accessToken = jwtService.generateToken(user)

                jwtService.revokeAllUserTokens(user?.id)

                jwtService.saveTokenUserModel(user?.id, accessToken)

                val authResponse = AuthenticationDTO()

                authResponse.accessToken = accessToken
                authResponse.refreshToken = refreshToken

                ObjectMapper().writeValue(response.outputStream, authResponse)
            }
        }
    }
}
