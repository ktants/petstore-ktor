package petstore.persistence

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

class VetSpecialty(id: EntityID<Long>) : LongEntity(id) {
    object Table : LongIdTable("vet_speciality") {
        val speciality = reference("specialty_id", Speciality.Table)
    }
    companion object : LongEntityClass<VetSpecialty>(Table)
}