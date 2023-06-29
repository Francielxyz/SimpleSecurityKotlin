package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import simple.security.kotlin.model.enums.Role
import java.util.*

@Data
@Builder
@Entity
@Table(name = "user")
class UserModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private var id: Long,

    @Column(name = "user_name", nullable = false)
    var userName: String,

    @Column(name = "user_password", nullable = false)
    var userPassword: String,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Enumerated(EnumType.STRING)
    private var role: Role? = null

    ) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return role?.getAuthorities()
    }

    override fun getUsername(): String = email

    override fun getPassword(): String = userPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}