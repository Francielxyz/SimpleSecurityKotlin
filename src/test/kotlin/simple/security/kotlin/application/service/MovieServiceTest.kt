package simple.security.kotlin.application.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import simple.security.kotlin.adapters.enums.Genre
import simple.security.kotlin.adapters.model.MovieModel
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.ports.output.MoviePersistencePort
import java.time.LocalDate

@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest(
    classes = [ServletWebServerFactoryAutoConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class MovieServiceTest {

    @InjectMocks
    private lateinit var service: MovieService

    @MockBean
    private lateinit var moviePersistencePort: MoviePersistencePort

    private var moviesMapper: ArrayList<MovieMapper> = arrayListOf()

    private lateinit var pageMoviesMapper: Page<MovieMapper>

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    @BeforeEach
    fun setMockOutput() {
        val localDate = LocalDate.now()
        moviesMapper.add(
            MovieMapper(
                id = 1,
                name = "teste",
                genre = Genre.ACTION,
                synopsis = "teste teste teste",
                duration = 120,
                launch = localDate
            )
        )
        moviesMapper.add(
            MovieMapper(
                id = 2,
                name = "teste 2",
                genre = Genre.BIOGRAPHY,
                synopsis = "teste teste teste",
                duration = 120,
                launch = localDate
            )
        )

        pageMoviesMapper = PageImpl(moviesMapper)
    }

    @Test
    fun testGetMovieById() {
        Mockito.`when`(moviePersistencePort.findById(any(Long::class.java))).thenReturn(moviesMapper[0])

        val movie = service.getById(1)
        assertEquals(movie?.id, moviesMapper[0].id)
    }

    @Test
    fun testFindAllMovies() {
        Mockito.`when`(moviePersistencePort.findAll(any(String::class.java), any(Pageable::class.java)))
            .thenReturn(pageMoviesMapper)
        val movies = service.findAll("", PageRequest.of(0, 10))

        assertEquals(movies.totalElements, 2)
        assertEquals(movies.content[0].id, pageMoviesMapper.content[0].id)
        assertEquals(movies.content[1].id, pageMoviesMapper.content[1].id)
    }

    @Test
    fun testSaveMovie() {
        Mockito.`when`(moviePersistencePort.save(any(MovieModel::class.java))).thenReturn(moviesMapper[0])

        val movie = service.save(moviesMapper[0])
        assertEquals(movie.id, moviesMapper[0].id)
    }

    @Test
    fun testUpdateMovie() {
        Mockito.`when`(moviePersistencePort.save(any(MovieModel::class.java))).thenReturn(moviesMapper[1])

        val movie = service.update(moviesMapper[1])
        assertEquals(movie.id, moviesMapper[1].id)
    }

    @Test
    fun testDeleteMovie() {
        Mockito.doNothing().`when`(moviePersistencePort).delete(any(Long::class.java))

        service.delete(moviesMapper[1].id!!)
    }
}