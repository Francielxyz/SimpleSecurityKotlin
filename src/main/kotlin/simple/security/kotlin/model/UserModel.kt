package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import simple.security.kotlin.model.abstract.AuditoriaModel
import simple.security.kotlin.model.enums.Role
import java.util.*

@Data
@Builder
@Entity
@Table(name = "user")
class UserModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "user_name", nullable = false)
    var userName: String? = null,

    @Column(name = "user_password", nullable = false)
    var userPassword: String? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String? = null,

    @Enumerated(EnumType.STRING)
    var role: Role? = null

) : UserDetails, AuditoriaModel() {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(role?.name))

    override fun getUsername(): String? = email

    override fun getPassword(): String? = userPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}