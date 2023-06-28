package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import simple.security.kotlin.model.enums.Role
import java.io.Serializable
import java.util.UUID

@Data
@Builder
@Entity
@Table(name = "role")
class RoleModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    var roleId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = true)
    var role: Role,

    ) : GrantedAuthority, Serializable {
    override fun getAuthority(): String = role.toString()
}