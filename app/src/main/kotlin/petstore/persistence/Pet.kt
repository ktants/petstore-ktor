package petstore.persistence

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date

class Pet(id: EntityID<Int>) : IntEntity(id) {
    object Table : IntIdTable("pet") {
        val name = varchar("name", 30).uniqueIndex()
        val birthday = date("birth_day")
        val petType = reference("type_id", PetType.Table)
        val petOwner = reference("owner_id", PetOwner.Table)
        val petGender = reference("gender_id", PetGender.Table)
    }
    companion object : IntEntityClass<Pet>(Table)
}