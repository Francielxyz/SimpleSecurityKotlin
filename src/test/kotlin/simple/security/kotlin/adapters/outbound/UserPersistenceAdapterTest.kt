package simple.security.kotlin.adapters.outbound

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.UserDTO
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.adapters.outbound.repository.UserRepository
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.application.mapper.UserMapper
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class UserPersistenceAdapterTest {

    @InjectMocks
    private lateinit var service: UserPersistenceAdapter

    @Mock
    private lateinit var repository: UserRepository

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    companion object {

        private var users: ArrayList<UserDTO> = arrayListOf()

        private var userMapper: UserMapper = UserMapper()

        private var userModel: UserModel = UserModel()

        @BeforeAll
        @JvmStatic
        fun setMockOutput() {
            val user = UserDTO(
                id = 1,
                userName = "teste",
                password = "teste",
                email = "teste@gmail.com",
                role = Role.USER,
            )

            users.add(user)

            userMapper = Converter.toModel(user, UserMapper::class.java)
            userModel = Converter.toModel(user, UserModel::class.java)
        }
    }

    @Test
    fun testSaveUser() {
        `when`(repository.save(any(UserModel::class.java))).thenReturn(userModel)

        assertEquals(Converter.toModel(service.save(userModel), MovieMapper::class.java).id, userMapper.id)
    }

    @Test
    fun testFindUserByEmail() {
        `when`(repository.findByEmail(any(String::class.java))).thenReturn(userModel)

        val movie = service.findByEmail(userMapper.email)?.let {
            Converter.toModel(it, UserMapper::class.java)
        }

        assertEquals(movie?.id, 1L)
        assertEquals(movie?.userName, "teste")
        assertEquals(movie?.password, "teste")
    }

    @Test
    fun testFindUserById() {
        `when`(repository.findById(any(Long::class.java))).thenReturn(Optional.of(userModel))

        val movie = service.findById(userMapper.id!!)?.let {
            Converter.toModel(it, UserMapper::class.java)
        }

        assertEquals(movie?.id, 1L)
        assertEquals(movie?.userName, "teste")
        assertEquals(movie?.password, "teste")
    }
}