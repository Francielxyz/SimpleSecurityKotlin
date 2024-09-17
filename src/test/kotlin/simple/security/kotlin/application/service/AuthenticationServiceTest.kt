package simple.security.kotlin.application.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.util.ReflectionTestUtils
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.exception.PersonalizedException
import simple.security.kotlin.application.extensions.getMessage
import simple.security.kotlin.application.mapper.AuthenticationMapper
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.JwtServicePort
import simple.security.kotlin.ports.output.UserPersistencePort
import java.util.*


@MockitoSettings(strictness = Strictness.LENIENT)
@ImportAutoConfiguration(MessageSourceAutoConfiguration::class)
@SpringBootTest(
    classes = [ServletWebServerFactoryAutoConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class AuthenticationServiceTest {

    @InjectMocks
    private lateinit var service: AuthenticationService

    @MockBean
    private lateinit var userIntegrationPort: UserPersistencePort

    @MockBean
    private lateinit var jwtService: JwtServicePort

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    private var request = MockHttpServletRequest()

    @Autowired
    private lateinit var messageSource: MessageSource

    private var authentications: ArrayList<AuthenticationMapper> = arrayListOf()

    private var usersMapper: ArrayList<UserMapper> = arrayListOf()

    private var userName = "teste teste"
    private var password = "teste123"
    private var email = "teste@gmail.com"

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @BeforeEach
    fun setMockOutput() {
        service = Mockito.spy(service)
        ReflectionTestUtils.setField(service, "messageSource", messageSource)

        usersMapper.add(
            UserMapper(id = 1, userName = userName, password = password, email = email, role = Role.USER)
        )

        val authenticationMapperToken = AuthenticationMapper(
            accessToken = "abcdef",
            refreshToken = "ghiklm"
        )

        val authenticationMapperRefreshToken = AuthenticationMapper(
            accessToken = "noprqs",
            refreshToken = "tuvwxyz"
        )

        authentications.add(authenticationMapperToken)
        authentications.add(authenticationMapperRefreshToken)

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer ${authenticationMapperRefreshToken.refreshToken}")
    }

    @Test
    fun testLoginService() {
        Mockito.`when`(userIntegrationPort.findByEmail(any(String::class.java))).thenReturn(usersMapper[0])
        Mockito.`when`(jwtService.generateToken(any(UserDetails::class.java), ArgumentMatchers.anyMap()))
            .thenReturn(authentications[0].accessToken)
        Mockito.`when`(jwtService.generateRefreshToken(any(UserDetails::class.java)))
            .thenReturn(authentications[0].refreshToken)

        val authenticationMapper = service.login(email, password)

        assertEquals(authenticationMapper.accessToken, authentications[0].accessToken)
        assertEquals(authenticationMapper.refreshToken, authentications[0].refreshToken)
    }

    @Test
    fun testLoginUsuarioInvalidoService() {
        try {
            Mockito.`when`(userIntegrationPort.findByEmail(any(String::class.java))).thenReturn(null)

            service.login(email, password)
        } catch (e: PersonalizedException) {
            assertEquals(e.message, messageSource.getMessage("erro.usuario.invalido"))
        }
    }

    @Test
    fun testRefreshToken() {
        Mockito.`when`(jwtService.extractUsername(any(String::class.java))).thenReturn(email)
        Mockito.`when`(userIntegrationPort.findByEmail(any(String::class.java))).thenReturn(usersMapper[0])
        Mockito.`when`(jwtService.isTokenValid(any(UserModel::class.java), any(String::class.java))).thenReturn(true)
        Mockito.`when`(jwtService.generateToken(any(UserModel::class.java), ArgumentMatchers.anyMap()))
            .thenReturn(authentications[1].accessToken)

        val authenticationMapper = service.refreshToken(request)

        assertEquals(authenticationMapper.accessToken, authentications[1].accessToken)
        assertEquals(authenticationMapper.refreshToken, authentications[1].refreshToken)
    }

}