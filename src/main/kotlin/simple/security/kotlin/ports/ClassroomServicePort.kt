package simple.security.kotlin.ports

import simple.security.kotlin.dto.ClassroomDTO

interface ClassroomServicePort {
    fun getById(id: Long): ClassroomDTO?

    fun save(classroomDTO: ClassroomDTO): ClassroomDTO

    fun getAll(): List<ClassroomDTO?>

    fun delete(id: Long)
}