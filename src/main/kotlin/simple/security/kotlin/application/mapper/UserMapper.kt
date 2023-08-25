package simple.security.kotlin.application.mapper

import simple.security.kotlin.adapters.enums.Role
import java.time.LocalDateTime

data class UserMapper(
    var id: Long? = null,
    var userName: String? = null,
    var password: String? = null,
    var email: String? = null,
    var role: Role? = null,
    var dataHoraCriacao: LocalDateTime? = null,
    var dataHoraUltimaAtualizacao: LocalDateTime? = null,
)