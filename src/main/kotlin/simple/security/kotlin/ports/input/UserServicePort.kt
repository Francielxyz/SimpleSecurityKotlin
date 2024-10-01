package simple.security.kotlin.ports.input

import simple.security.kotlin.application.mapper.UserMapper

interface UserServicePort {
    fun getById(id: Long): UserMapper?

    fun register(userMapper: UserMapper)

    fun update(userMapper: UserMapper)

    fun alterPassword(email: String, password: String)
}