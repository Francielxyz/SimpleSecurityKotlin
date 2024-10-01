package simple.security.kotlin.adapters.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import simple.security.kotlin.adapters.enums.Role

data class UserDTO(
    var id: Long? = null,
    var userName: String? = null,
    @JsonIgnore
    var password: String? = null,
    var email: String? = null,
    var role: Role? = null,
)