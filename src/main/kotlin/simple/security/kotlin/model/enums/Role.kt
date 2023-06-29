package simple.security.kotlin.model.enums

import lombok.Getter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.function.Function
import java.util.stream.Collectors

enum class Role {
    USER,
    ADMIN;

    @Getter
    private val permissions: Set<Permission>? = null

    open fun getAuthorities(): List<SimpleGrantedAuthority>? {
        val authorities = permissions
            ?.stream()
            ?.map {
                permission -> SimpleGrantedAuthority(permission.opcao)
            }
            ?.collect<List<SimpleGrantedAuthority>, Any>(Collectors.toList<SimpleGrantedAuthority>())
        authorities?.toSet(SimpleGrantedAuthority("ROLE_" + name))
        return authorities
    }
}