package simple.security.kotlin.ports.output

import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.mapper.UserMapper

interface UserPersistencePort {
    fun save(userModel: UserModel): UserMapper

    fun findByEmail(email: String?): UserMapper?

    fun findById(id: Long): UserMapper?
}