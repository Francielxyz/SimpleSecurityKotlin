package simple.security.kotlin.adapters.inbound.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.AuthenticationDTO
import simple.security.kotlin.adapters.dto.UserDTO
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.AuthenticationServicePort
import java.io.IOException

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthenticationController {

    @Autowired
    private lateinit var service: AuthenticationServicePort

    @PostMapping("/register")
    fun register(@RequestBody userDTO: UserDTO): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            service.register(Converter.toModel(userDTO, UserMapper::class.java))
        )

    @PostMapping("/authenticate")
    fun authenticate(@RequestParam email: String?, @RequestParam password: String?): ResponseEntity<AuthenticationDTO> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.authenticate(email, password).let {
                Converter.toModel(it, AuthenticationDTO::class.java)
            }
        )

    @PostMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.refreshToken(request, response)
        )

}