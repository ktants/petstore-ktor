package petstore.infrastructure.persistence.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import petstore.infrastructure.persistence.types.json

class Photo(id: EntityID<Int>) : IntEntity(id) {

    object Table : IntIdTable("photo") {
        val data = blob("data")
        val meta = json("meta")
    }

    companion object : IntEntityClass<Photo>(Table)
}


