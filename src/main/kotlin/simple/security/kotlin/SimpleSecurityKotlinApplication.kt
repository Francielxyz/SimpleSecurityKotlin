package simple.security.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleSecurityKotlinApplication

fun main(args: Array<String>) {
	runApplication<SimpleSecurityKotlinApplication>(*args)
}
