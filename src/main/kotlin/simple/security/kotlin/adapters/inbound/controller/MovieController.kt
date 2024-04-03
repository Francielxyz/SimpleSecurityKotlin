package simple.security.kotlin.adapters.inbound.controller

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.adapters.converter.Converter
import simple.security.kotlin.adapters.dto.MovieDTO
import simple.security.kotlin.application.mapper.MovieMapper
import simple.security.kotlin.ports.input.MovieServicePort

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
class MovieController {

    @Autowired
    private lateinit var service: MovieServicePort

    @GetMapping("/v1")
    fun movieById(@RequestParam(name = "id") id: Long): ResponseEntity<MovieDTO> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.getById(id)?.let {
                Converter.toModel(it, MovieDTO::class.java)
            }
        )

    @GetMapping("/v1/movies")
    fun movies(@RequestParam(required = false) search: String?, page: Pageable): Page<MovieDTO> =
        service.findAll(search, page).map {
            Converter.toModel(it, MovieDTO::class.java)
        }

    @PostMapping("/v1/save")
    fun save(@RequestBody movieDTO: MovieDTO): ResponseEntity<MovieDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            service.save(Converter.toModel(movieDTO, MovieMapper::class.java)).let {
                Converter.toModel(it, MovieDTO::class.java)
            }
        )

    @PutMapping("/v1/update")
    fun update(@RequestBody movieDTO: MovieDTO): ResponseEntity<MovieDTO> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.update(Converter.toModel(movieDTO, MovieMapper::class.java)).let {
                Converter.toModel(it, MovieDTO::class.java)
            }
        )

    @DeleteMapping("/v1/delete")
    fun delete(@RequestParam(name = "id") id: Long): ResponseEntity<Any> =
        ResponseEntity.status(HttpStatus.OK).body(
            service.delete(id)
        )
}