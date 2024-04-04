package simple.security.kotlin.application.exception

import org.springframework.http.HttpStatus

class PersonalizedException(
    var statusCode: HttpStatus? = HttpStatus.INTERNAL_SERVER_ERROR,
    override var message: String? = null
) : Exception(message)
