package simple.security.kotlin.ports.input

import simple.security.kotlin.application.mapper.UserMapper

interface UserServicePort {
    fun register(userMapper: UserMapper)

    fun update(userMapper: UserMapper)
}