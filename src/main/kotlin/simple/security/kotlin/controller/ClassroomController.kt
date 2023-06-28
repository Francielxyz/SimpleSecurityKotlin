package simple.security.kotlin.controller

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import simple.security.kotlin.dto.ClassroomDTO
import simple.security.kotlin.service.ClassroomService

@RestController
@RequestMapping("/classroom/v1")
@RequiredArgsConstructor
class ClassroomController {

    @Autowired
    private lateinit var classroomService: ClassroomService

    @PostMapping("/save")
    fun register(@RequestBody classroomDTO: ClassroomDTO): ResponseEntity<ClassroomDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(
            classroomService.save(classroomDTO)
        )


    // TODO - Fazer retorna um page
    @PostMapping("/all")
    fun authenticate(): List<ClassroomDTO?> =
        classroomService.getAll()

}