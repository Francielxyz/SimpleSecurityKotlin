package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Data
@Builder
@Entity
@Table(name = "user")
class UserModel(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private var userId: UUID,

    @Column(name = "user_name", nullable = false)
    var userName: String,

    @Column(name = "user_password", nullable = false)
    var userPassword: String,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "role_id")]
    )
    var roles: List<RoleModel> = mutableListOf(),

    ) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> = roles

    override fun getUsername(): String = email

    override fun getPassword(): String = userPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}