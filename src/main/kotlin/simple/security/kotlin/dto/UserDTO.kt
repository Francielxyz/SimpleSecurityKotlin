package simple.security.kotlin.dto

import simple.security.kotlin.model.enums.Role

data class UserDTO(
    var email: String? = null,
    var password: String? = null,
    var role: Role? = null
)