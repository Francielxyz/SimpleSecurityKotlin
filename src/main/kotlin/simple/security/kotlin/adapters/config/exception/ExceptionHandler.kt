package simple.security.kotlin.adapters.config.exception

import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import simple.security.kotlin.application.exception.PersonalizedException
import simple.security.kotlin.application.exception.ExceptionResponse
import simple.security.kotlin.application.extensions.getMessage

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @Autowired
    private lateinit var messageSource: MessageSource

    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(ex: Throwable, req: WebRequest): ResponseEntity<ExceptionResponse> {
        var statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        var mensagem = "Ocorreu um erro interno, tente novamente mais tarde."

        when (ex) {
            is PersonalizedException -> {
                statusCode = ex.statusCode ?: HttpStatus.INTERNAL_SERVER_ERROR
                mensagem = ex.message ?: mensagem
            }

            is BadCredentialsException -> {
                statusCode = HttpStatus.UNAUTHORIZED
                mensagem = messageSource.getMessage("erro.usuario.invalido")
            }

            is SignatureException -> {
                statusCode = HttpStatus.BAD_REQUEST
                mensagem = ex.message ?: mensagem
            }

            else -> logger.error("Exceção não esperada: ", ex)
        }

        return ResponseEntity.status(statusCode.value()).body(
            ExceptionResponse(statusCode.name, mensagem)
        )
    }
}
