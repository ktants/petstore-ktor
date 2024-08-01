package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class PetGender(id: EntityID<Int>) : IntEntity(id) {
    object Table : IntIdTable("pet_gender") {
        val name = varchar("name", 50).uniqueIndex()
    }
    companion object : IntEntityClass<PetGender>(Table)
}

