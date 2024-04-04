package simple.security.kotlin.ports.input

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import simple.security.kotlin.application.mapper.MovieMapper

interface MovieServicePort {
    fun getById(id: Long): MovieMapper?

    fun findAll(search: String?, page: Pageable): Page<MovieMapper>

    fun save(movieMapper: MovieMapper): MovieMapper

    fun update(movieMapper: MovieMapper): MovieMapper

    fun delete(id: Long)
}