package simple.security.kotlin.application.extensions

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

fun MessageSource.getMessage(code: String, args: Array<Any>? = null) =
    this.getMessage(code, args, LocaleContextHolder.getLocale())
