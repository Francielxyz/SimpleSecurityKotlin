package simple.security.kotlin.model

import jakarta.persistence.*
import simple.security.kotlin.model.abstract.AuditoriaModel

@Entity
@Table(name = "classroom")
class ClassroomModel(

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @Column(name = "class", nullable = false)
    var classRoom: String,

    @Column(name = "series", nullable = false)
    var series: String,

    ) : AuditoriaModel()