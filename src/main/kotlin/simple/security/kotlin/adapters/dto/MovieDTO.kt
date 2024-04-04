package simple.security.kotlin.adapters.dto

import simple.security.kotlin.adapters.enums.Genre
import java.time.LocalDate

data class MovieDTO(
    var id: Long? = null,
    var name: String? = null,
    var genre: Genre? = null,
    var synopsis: String? = null,
    var duration: Int? = null,
    var launch: LocalDate? = null,
)