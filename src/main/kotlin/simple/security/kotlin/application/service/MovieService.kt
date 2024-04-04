package simple.security.kotlin.application.service

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.model.MovieModel
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.ports.input.MovieServicePort
import simple.security.kotlin.ports.output.MoviePersistencePort

@Service
@RequiredArgsConstructor
class MovieService : MovieServicePort {

    @Autowired
    private lateinit var moviePersistencePort: MoviePersistencePort

    override fun getById(id: Long): MovieMapper? =
        moviePersistencePort.findById(id)

    override fun findAll(search: String?, page: Pageable): Page<MovieMapper> =
        moviePersistencePort.findAll(search, page)

    override fun save(movieMapper: MovieMapper): MovieMapper =
        moviePersistencePort.save(
            Converter.toModel(movieMapper, MovieModel::class.java)
        )

    override fun update(movieMapper: MovieMapper): MovieMapper =
        moviePersistencePort.save(
            Converter.toModel(movieMapper, MovieModel::class.java)
        )

    override fun delete(id: Long) =
        moviePersistencePort.delete(id)

}
