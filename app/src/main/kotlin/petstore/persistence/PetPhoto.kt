package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class PetPhoto(id: EntityID<Int>) : IntEntity(id) {

    object Table : IntIdTable("pet_photo") {
        val label = varchar("label", 80)
        val photo = reference("photo_id", Photo.Table)
    }

    companion object : IntEntityClass<PetPhoto>(Table)
}