package simple.security.kotlin.application.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.exception.PersonalizedException
import simple.security.kotlin.application.extensions.getMessage
import simple.security.kotlin.application.mapper.AuthenticationMapper
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.AuthenticationServicePort
import simple.security.kotlin.ports.input.JwtServicePort
import simple.security.kotlin.ports.output.AuthenticationIntegrationPort

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

    @Autowired
    private lateinit var messageSource: MessageSource

    @Transactional(rollbackFor = [Throwable::class])
    override fun register(userMapper: UserMapper) {
        userMapper.password = passwordEncoder.encode(userMapper.password)
        userMapper.role = Role.USER

        authenticationIntegration.save(Converter.toModel(userMapper, UserModel::class.java))
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun authenticate(email: String?, password: String?): AuthenticationMapper {
        val user = findByEmail(email)

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))

        return AuthenticationMapper().also {
            it.accessToken = jwtService.generateToken(user, createExtraClaims(user.role))
            it.refreshToken = jwtService.generateRefreshToken(user)
        }
    }

    override fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): AuthenticationMapper {
        val authenticationMapper = AuthenticationMapper()
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return authenticationMapper
        }

        val refreshToken = authHeader.substring(7)
        val user = findByEmail(jwtService.extractUsername(refreshToken))

        if (jwtService.isTokenValid(user, refreshToken)) {
            authenticationMapper.accessToken = jwtService.generateToken(user, createExtraClaims(user.role))
            authenticationMapper.refreshToken = refreshToken
        }

        return authenticationMapper
    }

    private fun findByEmail(userEmail: String?) = authenticationIntegration.findByEmail(userEmail)?.let {
        Converter.toModel(it, UserModel::class.java)
    } ?: throw PersonalizedException(HttpStatus.UNAUTHORIZED, messageSource.getMessage("erro.usuario.invalido"))

    private fun createExtraClaims(role: Role): Map<String, Any> = mapOf(
        "role" to role.opcao,
    )
}
