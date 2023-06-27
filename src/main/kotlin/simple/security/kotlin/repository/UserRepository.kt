package simple.security.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import simple.security.kotlin.model.UserModel
import java.util.Optional
import java.util.UUID

interface UserRepository : JpaRepository<UserModel, UUID> {
    fun findByEmail(email: String?): Optional<UserModel> //TODO - VÊ QUAL UTILIZAR
    fun findByUserName(username: String?): Optional<UserModel>
}