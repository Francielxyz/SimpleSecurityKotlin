package simple.security.kotlin.ports.output

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import simple.security.kotlin.adapters.model.MovieModel
import simple.security.kotlin.application.mapper.MovieMapper

interface MoviePersistencePort {
    fun findAll(search: String?, page: Pageable): Page<MovieMapper>

    fun findById(id: Long): MovieMapper?

    fun save(movieModel: MovieModel): MovieMapper

    fun delete(id: Long)
}