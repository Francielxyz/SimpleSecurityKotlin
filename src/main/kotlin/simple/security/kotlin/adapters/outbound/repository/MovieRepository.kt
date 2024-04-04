package simple.security.kotlin.adapters.outbound.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import simple.security.kotlin.adapters.model.MovieModel

interface MovieRepository : JpaRepository<MovieModel, Long> {
    @Query(
        """
            SELECT m 
            FROM MovieModel m
        """
    )
    fun findAllOptions(page: Pageable?): Page<MovieModel>

    @Query(
        """
        SELECT m 
        FROM MovieModel m
        WHERE
            (
                upper(m.name) like upper(CONCAT('%',:search,'%'))
                or upper(m.genre) like upper(CONCAT('%',:search,'%'))
                or upper(m.synopsis) like upper(CONCAT('%',:search,'%'))
                or DATE_FORMAT(m.launch, '%d/%m/%Y') like CONCAT('%',:search,'%')
                or DATE_FORMAT(m.dateTimeCreation, '%d/%m/%Y') like CONCAT('%',:search,'%')
                or DATE_FORMAT(m.dateTimeCreation, '%Y-%m-%d') like CONCAT('%',:search,'%')
                or DATE_FORMAT(m.dateTimeLastUpdate, '%d/%m/%Y %H:%i:%s') like CONCAT('%',:search,'%')
                or DATE_FORMAT(m.dateTimeLastUpdate, '%Y-%m-%d %H:%i:%s') like CONCAT('%',:search,'%')
            )
    """
    )
    fun findAllOptionsAndFields(@Param("search") search: String?, page: Pageable?): Page<MovieModel>
}