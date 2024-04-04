package simple.security.kotlin.application.service

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.enums.Role
import simple.security.kotlin.adapters.model.UserModel
import simple.security.kotlin.application.exception.PersonalizedException
import simple.security.kotlin.application.extensions.getMessage
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.UserServicePort
import simple.security.kotlin.ports.output.UserPersistencePort

@Service
@RequiredArgsConstructor
class UserService : UserServicePort {

    @Autowired
    private lateinit var userIntegrationPort: UserPersistencePort

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var messageSource: MessageSource

    override fun register(userMapper: UserMapper) {
        userIntegrationPort.findByEmail(userMapper.email)?.let {
            throw PersonalizedException(HttpStatus.BAD_REQUEST, messageSource.getMessage("erro.email.cadastrado"))
        }
        userMapper.password = passwordEncoder.encode(userMapper.password)
        userMapper.role = Role.USER

        userIntegrationPort.save(Converter.toModel(userMapper, UserModel::class.java))
    }

    override fun update(userMapper: UserMapper) {
        val userMapperUpdate = userIntegrationPort.findById(userMapper.id!!)?.also {
            it.userName = userMapper.userName
        } ?: throw PersonalizedException(HttpStatus.NOT_FOUND, messageSource.getMessage("erro.usuario.nao.encontrado"))

        userIntegrationPort.save(Converter.toModel(userMapperUpdate, UserModel::class.java))
    }

}
