package simple.security.kotlin.adapters.inbound.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
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
import simple.security.kotlin.application.mapper.AuthenticationMapper
import simple.security.kotlin.ports.input.AuthenticationServicePort
import java.util.Locale

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private lateinit var controller: AuthenticationController

    @MockBean
    private lateinit var service: AuthenticationServicePort

    private lateinit var mockMvc: MockMvc

    private var authentications: ArrayList<AuthenticationMapper> = arrayListOf()

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    val URL = "/authentication/v1/"

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
    }

    @Test
    fun testLogin() {
        Mockito.`when`(service.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(authentications[0])

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("${URL}login")
                .param("email", "abcd@gmail.com")
                .param("password", "1234")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").value("abcdef"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").value("ghiklm"))
    }

    @Test
    fun testRefreshToken() {
        Mockito.`when`(service.refreshToken(any(HttpServletRequest::class.java), any(HttpServletResponse::class.java))).thenReturn(authentications[1])

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("${URL}refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").value("noprqs"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").value("tuvwxyz"))
    }
}

