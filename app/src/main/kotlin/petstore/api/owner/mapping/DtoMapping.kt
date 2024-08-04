@file:JvmName("DtoMapping")

package petstore.api.owner.mapping

import petstore.api.owner.dto.OwnerDto
import petstore.infrastructure.persistence.entity.PetOwner


fun PetOwner.toDto(): OwnerDto.Simple = OwnerDto.Simple(
    id = this.id,
    name = formattedName(firstName, lastName),
    address = this.address,
    city = this.city,
    telephone = this.telephone ?: ""
)

fun formattedName(firstName: String?, lastName: String?): String = buildString {
    lastName?.let { append(it) }
    firstName?.let { append(" (", it, ")") }
}

fun PetOwner.formatedName(): String =
    buildString(firstName.length + lastName.length + 2) {
        lastName.takeUnless { it.isEmpty() }?.also { append(it) }
        firstName.takeUnless { it.isEmpty() }?.also { append(" (", it, ")") }
    }

