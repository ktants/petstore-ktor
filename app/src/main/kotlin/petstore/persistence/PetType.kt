package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class PetType(id : EntityID<Int>) : IntEntity(id) {

    object Table : IntIdTable("pet_type") {
        val name = varchar("name", 80).uniqueIndex()
    }

    companion object : IntEntityClass<PetType>(Table)
}