package simple.security.kotlin.model

import jakarta.persistence.*
import lombok.Builder
import lombok.Data
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import simple.security.kotlin.model.enums.Role

@Data
@Builder
@Entity
@Table(name = "user")
class UserModel(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false)
        var id: Long? = null,

        @Column(name = "name")
        var name: String? = null,

        @Column(name = "email", unique = true)
        var email: String? = null,

        @Column(name = "user_password")
        var userPassword: String? = null,

        @Column(name = "role")
        @Enumerated(EnumType.STRING)
        var role: Role? = null,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role!!.name))
    }

    override fun getUsername(): String {
        return email!!
    }

    override fun getPassword(): String {
        return userPassword!!
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}