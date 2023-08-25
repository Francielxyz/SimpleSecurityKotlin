package simple.security.kotlin.adapters.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.Builder
import lombok.Data
import simple.security.kotlin.adapters.enums.TokenType

@Data
@Builder
@Entity
@Table(name = "token")
class TokenModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "token", nullable = false)
    var token: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    var tokenType: TokenType? = null,

    @Column(name = "revoked")
    var revoked: Boolean? = null,

    @Column(name = "expired")
    var expired: Boolean? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserModel? = null,
)