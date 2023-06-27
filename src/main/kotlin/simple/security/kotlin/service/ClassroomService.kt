package simple.security.kotlin.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import simple.security.kotlin.dto.ClassroomDTO
import simple.security.kotlin.ports.ClassroomServicePort
import simple.security.kotlin.repository.ClassroomRepository

@Service
class ClassroomService : ClassroomServicePort {

    @Autowired
    private lateinit var classroomRepository: ClassroomRepository

    override fun getById(id: Long): ClassroomDTO? {
        TODO("Not yet implemented")
    }

    override fun save(classroomDTO: ClassroomDTO): ClassroomDTO {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<ClassroomDTO?> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}