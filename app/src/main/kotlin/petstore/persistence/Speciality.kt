package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class Speciality(id: EntityID<Int>) : IntEntity(id) {
    object Table : IntIdTable("speciality") {
        val name = varchar("name", 80)
    }
}