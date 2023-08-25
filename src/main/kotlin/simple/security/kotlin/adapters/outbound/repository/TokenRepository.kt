package simple.security.kotlin.adapters.outbound.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import simple.security.kotlin.adapters.model.TokenModel

interface TokenRepository : JpaRepository<TokenModel, Long> {

    @Query(
        """
            SELECT t FROM TokenModel t INNER JOIN UserModel u 
                ON t.user.id = u.id
                WHERE u.id = :id 
                AND (t.expired = FALSE OR t.revoked = FALSE)
        """
    )
    fun findAllValidTokenByUser(id: Long?): List<TokenModel>?

    fun findByToken(token: String?): TokenModel?
}