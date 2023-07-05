package simple.security.kotlin.service

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
import simple.security.kotlin.converter.Converter
import simple.security.kotlin.dto.AuthenticationDTO
import simple.security.kotlin.dto.TokenDTO
import simple.security.kotlin.dto.UserDTO
import simple.security.kotlin.model.TokenModel
import simple.security.kotlin.model.UserModel
import simple.security.kotlin.model.enums.Role
import simple.security.kotlin.model.enums.TokenType
import simple.security.kotlin.repository.TokenRepository
import simple.security.kotlin.repository.UserRepository
import java.io.IOException

@Service
@RequiredArgsConstructor
class AuthenticationService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @Autowired
    private lateinit var jwtService: JwtService

    private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var authenticationManager: AuthenticationManager

    fun register(userDTO: UserDTO): TokenDTO {
        userDTO.password = passwordEncoder.encode(userDTO.password)
        userDTO.role = Role.USER

        val userModel = Converter.toModel(userDTO, UserModel::class.java)

        val jwtToken = jwtService.generateToken(userModel)
        val refreshToken = jwtService.generateRefreshToken(userModel)

        saveUserToken(userRepository.save(userModel), jwtToken)

        return TokenDTO(
            token = jwtToken,
            refreshToken = refreshToken
        )
    }

    fun authenticate(userDTO: UserDTO?): TokenDTO {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userDTO?.email,
                userDTO?.password
            )
        )

        userRepository.findByEmail(userDTO?.email).also {
            val jwtToken = jwtService.generateToken(it)
            val refreshToken = jwtService.generateRefreshToken(it)

            revokeAllUserTokens(it)
            saveUserToken(it, jwtToken)

            return TokenDTO(
                token = jwtToken,
                refreshToken = refreshToken
            )
        }
    }

    private fun saveUserToken(userModel: UserModel?, jwtToken: String?) {
        tokenRepository.save(
            TokenModel(
                token = jwtToken,
                tokenType = TokenType.BEARER,
                revoked = false,
                expired = false,
                user = userModel,
            )
        )
    }

    private fun revokeAllUserTokens(userModel: UserModel?) {
        tokenRepository.findAllValidTokenByUser(userModel?.id).also {
            it.forEach { token ->
                token?.expired = true
                token?.revoked = true
            }

            tokenRepository.saveAll(it)
        }
    }

    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return
        }

        val refreshToken = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(refreshToken)

        if (userEmail != null) {
            val user = userRepository.findByEmail(userEmail)

            if (jwtService.isTokenValid(refreshToken, user)) {
                val accessToken = jwtService.generateToken(user)

                revokeAllUserTokens(user)

                saveUserToken(user, accessToken)

                val authResponse = AuthenticationDTO()

                authResponse.accessToken = accessToken
                authResponse.refreshToken = refreshToken

                ObjectMapper().writeValue(response.outputStream, authResponse)
            }
        }
    }
}
