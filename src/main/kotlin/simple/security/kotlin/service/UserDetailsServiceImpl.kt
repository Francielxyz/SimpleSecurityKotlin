package simple.security.kotlin.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import simple.security.kotlin.repository.UserRepository

@Service
@Transactional
abstract class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails? {
        val userModel = userRepository.findByUserName(username).orElseThrow {
            UsernameNotFoundException("User Not Found with username: $username")
        }

        return User(
            userModel.username,
            userModel.password,
            true,
            true,
            true,
            true,
            userModel.authorities
        )
    }
}