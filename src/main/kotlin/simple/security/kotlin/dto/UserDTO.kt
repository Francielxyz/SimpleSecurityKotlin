//package simple.security.kotlin.dto
//
//import simple.security.kotlin.model.UserModel
//import simple.security.kotlin.model.enums.Role
//
//data class UserDTO(
//        val id: Long? = null,
//        val name: String? = null,
//        val email: String? = null,
//        var password: String? = null,
//        var role: Role? = null
//) {
//    fun toEntity(passwordEncoder: String, role: Role?): UserModel =
//            UserModel(
//                    this.id,
//                    this.name,
//                    this.email,
//                    passwordEncoder,
//                    role,
//            )
//}