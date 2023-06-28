//package simple.security.kotlin.service
//
//import lombok.RequiredArgsConstructor
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.security.authentication.AuthenticationManager
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.stereotype.Service
//import simple.security.kotlin.dto.TokenDTO
//import simple.security.kotlin.dto.UserDTO
//import simple.security.kotlin.model.TokenModel
//import simple.security.kotlin.model.UserModel
//import simple.security.kotlin.model.enums.Role
//import simple.security.kotlin.model.enums.Token
//import simple.security.kotlin.repository.TokenRepository
//import simple.security.kotlin.repository.UserRepository
//
//@Service
//@RequiredArgsConstructor
//class AuthenticationService {
//
//    @Autowired
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var tokenRepository: TokenRepository
//
//    @Autowired
//    private lateinit var passwordEncoder: PasswordEncoder
//
//    @Autowired
//    private lateinit var jwtService: JwtService
//
//    @Autowired
//    private lateinit var authenticationManager: AuthenticationManager
//
//    fun register(userDTO: UserDTO): TokenDTO {
//        val userEntity = userDTO.toEntity(
//                passwordEncoder.encode(userDTO.password),
//                Role.USER
//        )
//
//        val tokenDTO = TokenDTO(token = jwtService.generateToken(userEntity))
//
//        saveUserToken(userRepository.save(userEntity), tokenDTO.token!!)
//
//        return tokenDTO
//    }
//
//    fun authenticate(userDTO: UserDTO): TokenDTO {
//        authenticationManager.authenticate(
//                UsernamePasswordAuthenticationToken(
//                        userDTO.email,
//                        userDTO.password
//                )
//        )
//
//        userRepository.findByEmail(userDTO.email).also {
//            val tokenDTO = TokenDTO(token = jwtService.generateToken(it.orElseThrow()))
//
//            revokeAllUserTokens(it.orElseThrow())
//            saveUserToken(it.orElseThrow(), tokenDTO.token!!)
//
//            return tokenDTO
//        }
//    }
//
//    private fun saveUserToken(userModel: UserModel, jwtToken: String) {
//        tokenRepository.save(
//                TokenModel(
//                        token = jwtToken,
//                        tokenType = Token.BEARER,
//                        expired = false,
//                        revoked = false,
//                        userModel = userModel
//                )
//        )
//    }
//
//    private fun revokeAllUserTokens(userModel: UserModel) {
//        tokenRepository.findAllValidTokenByUser(userModel.id).also {
//            it.forEach { token ->
//                token.expired = true
//                token.revoked = true
//            }
//
//            tokenRepository.saveAll(it)
//        }
//
//        return
//    }
//}
