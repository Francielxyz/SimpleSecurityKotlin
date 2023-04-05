package simple.security.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import simple.security.kotlin.model.TokenModel
import java.util.*

interface TokenRepository : JpaRepository<TokenModel, Long> {

    @Query(
            """
            select t from TokenModel t inner join UserModel u 
            on t.userModel.id = u.id 
            where u.id = :id and (t.expired = false or t.revoked = false)
            """
    )
    fun findAllValidTokenByUser(id: Long?): List<TokenModel>

    fun findByToken(token: String?): Optional<TokenModel>
}