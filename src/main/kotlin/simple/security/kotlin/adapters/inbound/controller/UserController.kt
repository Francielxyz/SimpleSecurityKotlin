package simple.security.kotlin.adapters.inbound.controller

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.UserDTO
import simple.security.kotlin.application.mapper.UserMapper
import simple.security.kotlin.ports.input.UserServicePort

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
class UserController {

    @Autowired
    private lateinit var service: UserServicePort

    @GetMapping("/v1")
    fun userById(@RequestParam(name = "id") id: Long): ResponseEntity<UserDTO> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.getById(id)?.let {
                Converter.toModel(it, UserDTO::class.java)
            }
        )

    @PostMapping("/v1/register")
    fun register(@RequestBody userDTO: UserDTO): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            service.register(Converter.toModel(userDTO, UserMapper::class.java))
        )

    @PutMapping("/v1/update")
    fun update(@RequestBody userDTO: UserDTO): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.update(Converter.toModel(userDTO, UserMapper::class.java))
        )

    @PutMapping("/v1/alter-password")
    fun alterPassword(@RequestParam email: String, @RequestParam password: String): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.alterPassword(email, password)
        )
}