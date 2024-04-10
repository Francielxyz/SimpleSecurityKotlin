package simple.security.kotlin.adapters.inbound.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import simple.security.kotlin.adapters.config.WebConfig
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.UserDTO
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.UserServicePort
import java.util.Locale

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private lateinit var controller: UserController

    @MockBean
    private lateinit var service: UserServicePort

    private lateinit var mockMvc: MockMvc

    private var users: ArrayList<UserDTO> = arrayListOf()

    private var userMapper: UserMapper = UserMapper()

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    val URL = "/user/v1/"

    @BeforeEach
    fun setMockOutput() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
            .setViewResolvers(object : ViewResolver {
                override fun resolveViewName(viewName: String, locale: Locale): View {
                    return Jackson2ObjectMapperBuilder().build()
                }
            })
            .build()

        val user = UserDTO(
            id = 1,
            userName = "teste",
            password = "teste",
            email = "teste@gmail.com",
            role = Role.USER,
        )

        users.add(user)

        userMapper = Converter.toModel(user, UserMapper::class.java)
    }

    @Test
    fun testRegisterUser() {
        Mockito.doNothing().`when`(service).register(userMapper)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("${URL}register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebConfig().objectMapperLowerCamelCase().writeValueAsString(users[0]))
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun testUpdateUser() {
        Mockito.doNothing().`when`(service).update(userMapper)
        users[0].userName = "Teste Update"

        mockMvc.perform(
            MockMvcRequestBuilders
                .put("${URL}update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebConfig().objectMapperLowerCamelCase().writeValueAsString(users[0]))
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}