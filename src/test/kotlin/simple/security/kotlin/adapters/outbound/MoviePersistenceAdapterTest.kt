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
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.MovieDTO
import simple.security.kotlin.adapters.enums.Genre
import simple.security.kotlin.adapters.model.MovieModel
import simple.security.kotlin.adapters.outbound.repository.MovieRepository
import simple.security.kotlin.application.mapper.MovieMapper
import java.time.LocalDate
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@EnableSpringDataWebSupport
@ActiveProfiles("test")
class MoviePersistenceAdapterTest {

    @InjectMocks
    private lateinit var service: MoviePersistenceAdapter

    @Mock
    private lateinit var repository: MovieRepository

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

    companion object {

        private var movies: ArrayList<MovieDTO> = arrayListOf()

        private var moviesMapper: ArrayList<MovieMapper> = arrayListOf()

        private var moviesModel: ArrayList<MovieModel> = arrayListOf()

        private lateinit var pageMoviesModel: Page<MovieModel>

        @BeforeAll
        @JvmStatic
        fun setMockOutput() {
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
                name = "Spring Boot",
                genre = Genre.ACTION,
                synopsis = "teste teste teste",
                duration = 160,
                launch = LocalDate.now()
            )

            movies.add(movie1)
            movies.add(movie2)

            moviesMapper.add(Converter.toModel(movie1, MovieMapper::class.java))
            moviesMapper.add(Converter.toModel(movie2, MovieMapper::class.java))
            moviesModel.addAll(moviesMapper.map {
                Converter.toModel(it, MovieModel::class.java)
            })
            pageMoviesModel = PageImpl(moviesModel)
        }
    }

    @Test
    fun testFindAll() {
        `when`(repository.findAllOptions(any(Pageable::class.java))).thenReturn(pageMoviesModel)

        val moviesPage = service.findAll(null, Pageable.unpaged()).map {
            Converter.toModel(it, MovieMapper::class.java)
        }

        assertEquals(moviesPage.totalElements, 2)
        assertEquals(moviesPage.totalPages, 1)
        assertEquals(moviesPage.content[0].id, 1)
        assertEquals(moviesPage.content[1].id, 2)
    }

    @Test
    fun testFindAllBySearch() {
        val pageable: Pageable = Pageable.unpaged()
        val moviesModel2: ArrayList<MovieModel> = arrayListOf()
        moviesModel2.add(moviesModel[1])

        `when`(repository.findAllOptionsAndFields("boot", pageable)).thenReturn(PageImpl(moviesModel2))

        val moviesPage = service.findAll("boot", pageable).map {
            Converter.toModel(it, MovieMapper::class.java)
        }

        assertEquals(moviesPage.totalElements, 1)
        assertEquals(moviesPage.totalPages, 1)
        assertEquals(moviesPage.content[0].id, 2)
    }

    @Test
    fun testFindMovieById() {
        `when`(repository.findById(any(Long::class.java))).thenReturn(Optional.of(moviesModel[1]))

        val movie = service.findById(moviesMapper[1].id!!)?.let {
            Converter.toModel(it, MovieMapper::class.java)
        }

        assertEquals(movie?.id, 2L)
        assertEquals(movie?.genre, Genre.ACTION)
        assertEquals(movie?.duration, 160)
    }

    @Test
    fun testSaveMovie() {
        `when`(repository.save(any(MovieModel::class.java))).thenReturn(moviesModel[0])

        assertEquals(Converter.toModel(service.save(moviesModel[0]), MovieMapper::class.java), moviesMapper[0])
    }

    @Test
    fun testDeleteMovieById() {
        service.delete(1L)

        Mockito.verify(repository).deleteById(any(Long::class.java))
    }
}