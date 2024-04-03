package simple.security.kotlin.adapters.outbound

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.model.MovieModel
import simple.security.kotlin.adapters.outbound.repository.MovieRepository
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.ports.output.MoviePersistencePort

@Component
class MoviePersistenceAdapter : MoviePersistencePort {

    @Autowired
    private lateinit var movieRepository: MovieRepository

    override fun findAll(search: String?, page: Pageable): Page<MovieMapper> {
        val movies = if (search?.isNotBlank() == true) {
            movieRepository.findAllOptionsAndFields(search, page)
        } else {
            movieRepository.findAllOptions(page)
        }

        return movies.map {
            Converter.toModel(it, MovieMapper::class.java)
        }
    }

    override fun findById(id: Long): MovieMapper? =
        movieRepository.findById(id).let {
            Converter.toModel(it, MovieMapper::class.java)
        }

    @Transactional(rollbackFor = [Throwable::class], propagation = Propagation.REQUIRES_NEW)
    override fun save(movieModel: MovieModel): MovieMapper =
        Converter.toModel(movieRepository.save(movieModel), MovieMapper::class.java)

    @Transactional(rollbackFor = [Throwable::class], propagation = Propagation.REQUIRES_NEW)
    override fun delete(id: Long) =
        movieRepository.deleteById(id)

}