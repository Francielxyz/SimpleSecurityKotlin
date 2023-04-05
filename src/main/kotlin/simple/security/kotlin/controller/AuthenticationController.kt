package simple.security.kotlin.controller

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.dto.TokenDTO
import simple.security.kotlin.dto.UserDTO
import simple.security.kotlin.service.AuthenticationService

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class AuthenticationController {

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @PostMapping("/register")
    fun register(@RequestBody userDTO: UserDTO): ResponseEntity<TokenDTO?>? {
        return ResponseEntity.ok(authenticationService.register(userDTO))
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody userDTO: UserDTO): ResponseEntity<TokenDTO?>? {
        return ResponseEntity.ok(authenticationService.authenticate(userDTO))
    }
}