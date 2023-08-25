package simple.security.kotlin.adapters.dto

import simple.security.kotlin.adapters.enums.Role

data class UserDTO(
    var id: Long? = null,
    var userName: String? = null,
    var password: String? = null,
    var email: String? = null,
    var role: Role? = null
)