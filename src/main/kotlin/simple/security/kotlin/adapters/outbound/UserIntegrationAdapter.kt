package simple.security.kotlin.adapters.outbound

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.adapters.outbound.repository.UserRepository
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.output.UserIntegrationPort

@Component
class UserIntegrationAdapter : UserIntegrationPort {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun save(userModel: UserModel): UserMapper =
        Converter.toModel(userRepository.save(userModel), UserMapper::class.java)

    override fun findByEmail(email: String?): UserMapper? =
        userRepository.findByEmail(email)?.let {
            Converter.toModel(it, UserMapper::class.java)
        }

    override fun findById(id: Long): UserMapper? =
        userRepository.findById(id).let {
            Converter.toModel(it, UserMapper::class.java)
        }
}