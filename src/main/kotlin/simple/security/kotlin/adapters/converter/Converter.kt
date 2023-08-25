package simple.security.kotlin.adapters.converter

import org.modelmapper.ModelMapper
import reactor.core.publisher.Flux

object Converter {
    private val modelMapper = ModelMapper()

    fun <D> toFluxCollection(objs: Flux<Any>, outClass: Class<D>) =
        objs.flatMap { objs }.mapNotNull { user -> toModel(user, outClass) }

    fun <D> toModel(obj: Any, outClass: Class<D>) = modelMapper.map(obj, outClass)

    fun <D> toCollection(objs: List<Any>, outClass: Class<D>) = objs.map { toModel(it, outClass) }.toMutableList()
}
