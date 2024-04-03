package simple.security.kotlin.adapters.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import simple.security.kotlin.adapters.enums.Genre
import simple.security.kotlin.adapters.model.abstract.AuditoriaModel
import java.time.LocalDate

@Entity
@Table(name = "movies")
class MovieModel(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "name")
    var name: String? = null,

    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    var genre: Genre? = null,

    @Column(name = "synopsis")
    var synopsis: String? = null,

    @Column(name = "duration")
    var duration: Int? = null,

    @Column(name = "launch")
    var launch: LocalDate? = null,
) : AuditoriaModel()