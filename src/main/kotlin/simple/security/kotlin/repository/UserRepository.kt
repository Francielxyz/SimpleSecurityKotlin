package simple.security.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import simple.security.kotlin.model.UserModel
import java.util.Optional

interface UserRepository : JpaRepository<UserModel, Long> {
    fun findByEmail(email: String?): Optional<UserModel>
}