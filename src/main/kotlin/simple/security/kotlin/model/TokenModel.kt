package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import simple.security.kotlin.model.enums.Token

@Data
@Builder
@Entity
class TokenModel(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        var id: Long? = null,

        @Column(name = "token", unique = true)
        var token: String? = null,

        @Column(name = "token_type")
        @Enumerated(EnumType.STRING)
        var tokenType: Token? = null,

        @Column(name = "revoked")
        var revoked: Boolean? = null,

        @Column(name = "expired")
        var expired: Boolean? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var userModel: UserModel? = null,
)