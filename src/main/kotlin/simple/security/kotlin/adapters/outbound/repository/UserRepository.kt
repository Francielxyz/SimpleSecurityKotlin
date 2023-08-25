package simple.security.kotlin.adapters.outbound.repository

import org.springframework.data.jpa.repository.JpaRepository
import simple.security.kotlin.adapters.model.UserModel

interface UserRepository : JpaRepository<UserModel, Long> {
    fun findByEmail(email: String?): UserModel?
}