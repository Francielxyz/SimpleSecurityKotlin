package simple.security.kotlin.application.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.util.ReflectionTestUtils
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.exception.PersonalizedException
import simple.security.kotlin.application.extensions.getMessage
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.output.UserPersistencePort
import java.time.LocalDateTime

@MockitoSettings(strictness = Strictness.LENIENT)
@ImportAutoConfiguration(MessageSourceAutoConfiguration::class)
@SpringBootTest(
    classes = [ServletWebServerFactoryAutoConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class UserServiceTest {
    @InjectMocks
    private lateinit var service: UserService

    @MockBean
    private lateinit var userIntegrationPort: UserPersistencePort

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var messageSource: MessageSource

    private var usersMapper: ArrayList<UserMapper> = arrayListOf()

    private lateinit var pageUsersMapper: Page<UserMapper>

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @BeforeEach
    fun setMockOutput() {
        service = Mockito.spy(service)
        ReflectionTestUtils.setField(service, "messageSource", messageSource)

        val localDateTime = LocalDateTime.now()
        usersMapper.add(
            UserMapper(
                id = 1,
                userName = "teste",
                password = "teste",
                email = "teste@gmail.com",
                role = Role.USER,
                dataHoraCriacao = localDateTime,
                dataHoraUltimaAtualizacao = localDateTime
            )
        )

        usersMapper.add(
            UserMapper(
                id = 2,
                userName = "test2e",
                password = "teste2",
                email = "teste2@gmail.com",
                role = Role.USER,
                dataHoraCriacao = localDateTime,
                dataHoraUltimaAtualizacao = localDateTime
            )
        )


        pageUsersMapper = PageImpl(usersMapper)
    }

    @Test
    fun testGetUserById() {
        Mockito.`when`(userIntegrationPort.findById(any(Long::class.java))).thenReturn(usersMapper[0])

        val user = service.getById(1)
        Assertions.assertEquals(user?.id, usersMapper[0].id)
    }

    @Test
    fun testSaveUser() {
        Mockito.`when`(userIntegrationPort.findByEmail(any(String::class.java))).thenReturn(null)
        Mockito.`when`(userIntegrationPort.save(any(UserModel::class.java))).thenReturn(usersMapper[0])
        Mockito.`when`(passwordEncoder.encode(any(String::class.java))).thenReturn("*//*-/--245040")

        service.register(usersMapper[0])
    }

    @Test
    fun testUserExist() {
        try {
            Mockito.`when`(userIntegrationPort.findByEmail(any(String::class.java))).thenReturn(usersMapper[0])

            service.register(usersMapper[0])
        } catch (e: PersonalizedException) {
            Assertions.assertEquals(e.message, messageSource.getMessage("erro.email.cadastrado"))
        }
    }

    @Test
    fun testUpdateUser() {
        usersMapper[1].userName = "teste teste"
        Mockito.`when`(userIntegrationPort.findById(any(Long::class.java))).thenReturn(usersMapper[1])
        Mockito.`when`(userIntegrationPort.save(any(UserModel::class.java))).thenReturn(usersMapper[1])

        service.update(usersMapper[1])
    }

    @Test
    fun testUpdateUserNotExist() {
        try {
            Mockito.`when`(userIntegrationPort.findById(any(Long::class.java))).thenReturn(null)

            service.update(usersMapper[0])
        } catch (e: PersonalizedException) {
            Assertions.assertEquals(e.message, messageSource.getMessage("erro.usuario.nao.encontrado"))
        }
    }

}