package petstore.infrastructure.persistence.entity

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

class PetVisit(id: EntityID<Long>) : LongEntity(id) {

    object Table : LongIdTable("pet_visit") {
        val visitDate = date("visit_date")
        val description = text("description")
        val pet = reference("pet_id", Pet.Table)
    }

    companion object : LongEntityClass<PetVisit>(Table)
}