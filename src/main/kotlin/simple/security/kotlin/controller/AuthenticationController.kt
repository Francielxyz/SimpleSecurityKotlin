package simple.security.kotlin.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.dto.AuthenticationDTO
import simple.security.kotlin.dto.UserDTO
import simple.security.kotlin.service.AuthenticationService
import java.io.IOException

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthenticationController {

    @Autowired
    private lateinit var service: AuthenticationService

    @PostMapping("/register")
    fun register(@RequestBody userDTO: UserDTO): ResponseEntity<AuthenticationDTO> =
        ResponseEntity.ok(service.register(userDTO))

    @PostMapping("/authenticate")
    fun authenticate(@RequestParam email: String?, @RequestParam password: String?): ResponseEntity<AuthenticationDTO> =
        ResponseEntity.ok(service.authenticate(email, password))

    @PostMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse) =
        service.refreshToken(request, response)

}