package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class PetOwner(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PetOwner>(Table)
    object Table : IntIdTable("pet_owner") {
        val firstName = varchar("first_name", 30)
        val lastName = varchar("last_name", 30).uniqueIndex()
        val address = varchar("address", 255)
        val city = varchar("city", 80)
    }
}

