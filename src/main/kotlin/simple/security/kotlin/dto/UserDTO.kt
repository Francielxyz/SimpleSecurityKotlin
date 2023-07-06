package simple.security.kotlin.dto

import simple.security.kotlin.model.enums.Role

data class UserDTO(
    var id: Long? = null,
    var userName: String? = null,
    var userPassword: String? = null,
    var email: String? = null,
    var role: Role? = null
)