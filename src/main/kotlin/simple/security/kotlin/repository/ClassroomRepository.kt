package simple.security.kotlin.repository

import org.springframework.data.jpa.repository.JpaRepository
import simple.security.kotlin.model.ClassroomModel

interface ClassroomRepository : JpaRepository<ClassroomModel, Long>