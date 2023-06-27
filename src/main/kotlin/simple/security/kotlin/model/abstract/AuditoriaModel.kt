package simple.security.kotlin.model.abstract

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class AuditoriaModel(

    @Column(name = "data_hora_criacao", updatable = false)
    @CreatedDate
    open var dataHoraCriacao: LocalDateTime? = null,

    @Column(name = "data_hora_ultima_atualizacao")
    @LastModifiedDate
    open var dataHoraUltimaAtualizacao: LocalDateTime? = null
)