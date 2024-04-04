package simple.security.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class SimpleSecurityKotlinApplication

fun main(args: Array<String>) {
	runApplication<SimpleSecurityKotlinApplication>(*args)
}

// TODO - Criar Testes, Não salvar token, acrescentar no token tempo de expiração e roles, criar novo end point com acesso/permissões