package simple.security.kotlin.adapters.inbound.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
import simple.security.kotlin.adapters.dto.MovieDTO
import simple.security.kotlin.adapters.enums.Genre
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.ports.input.MovieServicePort
import java.time.LocalDate
import java.util.Locale

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class MovieControllerTest {

    @Autowired
    private lateinit var controller: MovieController

    @MockBean
    private lateinit var service: MovieServicePort

    private lateinit var mockMvc: MockMvc

    private var movies: ArrayList<MovieDTO> = arrayListOf()

    private var moviesMapper: ArrayList<MovieMapper> = arrayListOf()

    private lateinit var pageMoviesMapper: Page<MovieMapper>

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    val URL = "/movie/v1"

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

        val movie1 = MovieDTO(
            id = 1,
            name = "Spring 1",
            genre = Genre.COMEDY,
            synopsis = "teste teste teste",
            duration = 120,
            launch = LocalDate.now()
        )

        val movie2 = MovieDTO(
            id = 2,
            name = "Spring 2",
            genre = Genre.ACTION,
            synopsis = "teste teste teste",
            duration = 160,
            launch = LocalDate.now()
        )

        movies.add(movie1)
        movies.add(movie2)

        moviesMapper.add(Converter.toModel(movie1, MovieMapper::class.java))
        moviesMapper.add(Converter.toModel(movie2, MovieMapper::class.java))
        pageMoviesMapper = PageImpl(moviesMapper)
    }

    @Test
    fun testMovieById() {
        Mockito.`when`(service.getById(ArgumentMatchers.anyLong())).thenReturn(moviesMapper[0])

        mockMvc.perform(
            MockMvcRequestBuilders
                .get(URL)
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Spring 1"))
    }

    @Test
    fun testGetAllMovies() {
        Mockito.`when`(service.findAll(ArgumentMatchers.anyString(), any(Pageable::class.java))).thenReturn(pageMoviesMapper)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("${URL}/movies")
                .param("search", "")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.size").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value("2"))
    }

    @Test
    fun testSaveMovie() {
        Mockito.`when`(service.save(any(MovieMapper::class.java))).thenReturn(moviesMapper[0])

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("${URL}/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebConfig().objectMapperLowerCamelCase().writeValueAsString(movies[0]))
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Spring 1"))
    }

    @Test
    fun testUpdateMovie() {
        Mockito.`when`(service.update(any(MovieMapper::class.java))).thenReturn(moviesMapper[1])

        mockMvc.perform(
            MockMvcRequestBuilders
                .put("${URL}/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(WebConfig().objectMapperLowerCamelCase().writeValueAsString(movies[1]))
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Spring 2"))
    }

    @Test
    fun testDeleteMovie() {
        Mockito.doNothing().`when`(service).delete(ArgumentMatchers.anyLong())

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("${URL}/delete")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}