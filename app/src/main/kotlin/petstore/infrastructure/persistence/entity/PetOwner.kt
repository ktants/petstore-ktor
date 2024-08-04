package petstore.infrastructure.persistence.entity

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SizedIterable

class PetOwner(id: EntityID<Int>) : IntEntity(id) {

    var firstName: String by Table.firstName
    var lastName: String by Table.lastName
    var address: String by Table.address
    var city: String by Table.city
    var pets: SizedIterable<Pet> by Pet.via(Pet.Table)
    var telephone: String? by Table.telephone

    companion object : IntEntityClass<PetOwner>(Table)

    object Table : IntIdTable("pet_owner") {
        val firstName = varchar("first_name", 30)
        val lastName = varchar("last_name", 30).uniqueIndex()
        val address = varchar("address", 255)
        val city = varchar("city", 80)
        val telephone = varchar("telephone", 20).nullable()
    }
}

