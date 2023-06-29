package simple.security.kotlin.model

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
import simple.security.kotlin.model.enums.TokenType

@Data
@Builder
@Entity
@Table(name = "token")
class TokenModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long,

    @Column(name = "token", nullable = false)
    var token: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type")
    var tokenType: TokenType? = null,

    @Column(name = "revoked")
    var revoked: Boolean? = null,

    @Column(name = "token_type")
    var expired: Boolean? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserModel? = null,
)