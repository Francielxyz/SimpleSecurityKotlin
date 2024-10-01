package simple.security.kotlin.adapters.inbound.controller

import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.AuthenticationDTO
import simple.security.kotlin.ports.input.AuthenticationServicePort

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
class AuthenticationController {

    @Autowired
    private lateinit var service: AuthenticationServicePort

    @GetMapping("/v1/login")
    fun login(@RequestParam email: String?, @RequestParam password: String?): ResponseEntity<AuthenticationDTO> {
        val authenticationDTO = service.login(email, password).let {
            Converter.toModel(it, AuthenticationDTO::class.java)
        }
        return ResponseEntity.status(HttpStatus.OK)
            .headers(HttpHeaders().also {
                it.add("access_token", authenticationDTO.accessToken)
                it.add("refresh_token", authenticationDTO.refreshToken)
            })
            .body(authenticationDTO)
    }

    @GetMapping("/v1/refresh-token")
    fun refreshToken(request: HttpServletRequest?): ResponseEntity<AuthenticationDTO> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.refreshToken(request).let {
                Converter.toModel(it, AuthenticationDTO::class.java)
            }
        )

}