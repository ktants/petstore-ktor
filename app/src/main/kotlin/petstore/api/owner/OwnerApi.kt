package petstore.api.owner

import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.andIfNotNull
import org.jetbrains.exposed.sql.emptySized
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.transactions.transaction
import petsrore.commons.api.Pageable
import petsrore.commons.api.Response
import petsrore.commons.api.slice
import petstore.api.owner.dto.NewOwnerDto
import petstore.api.owner.dto.OwnerDto
import petstore.api.owner.mapping.formatedName
import petstore.api.owner.mapping.formattedName
import petstore.api.owner.mapping.toDto
import petstore.infrastructure.persistence.entity.Pet
import petstore.infrastructure.persistence.entity.PetOwner

class OwnerApi {

    fun find(query: String?, pageable: Pageable): Response<*> {

        return if (pageable.pageNo <= 0)
            Response.Error(
                "error_page_invalid",
                "Page($pageable.pageNo) must be 1 or more."
            )
        else if (pageable.pageSize < 0)
            Response.Error(
                "error_page_size_invalid",
                "Page size ($pageable.pageSize) must be 0 or more."
            )
        else PetOwner.find(findOwnersCriteria(query))
            .orderBy(PetOwner.Table.lastName to SortOrder.ASC)
            .limit(pageable)
            .map { petOwner ->
                OwnerDto.Listing(
                    id = petOwner.id,
                    name = petOwner.formatedName(),
                    address = petOwner.address,
                    city = petOwner.city,
                    telephone = petOwner.telephone.orEmpty(),
                    pets = petOwner.pets.orderBy(Pet.Table.name to SortOrder.ASC).map { it.name }.toList()
                )
            }.let { Response.Part(it, pageable.location(), false) }

    }

    fun ownerById(ownerId: String?): Response<*> {

        if (ownerId.isNullOrBlank() || ownerId.isEmpty())
            return Response.Error(
                errorCode = "error_owner_id_invalid",
                errorMessage = "owner id($ownerId) can't be null, blank or empty"
            )

        val id = ownerId.toIntOrNull() ?: return Response.Error(
            errorCode = "error_owner_id_invalid",
            errorMessage = "owner id($ownerId) must be a numeric integer"
        )

        val owner =
            PetOwner.findById(id)?.let {
                OwnerDto.Simple(
                    id = it.id,
                    name = it.formatedName(),
                    address = it.address,
                    city = it.city,
                    telephone = it.telephone ?: ""
                )
            } ?: return Response.Error(
                errorCode = "error_owner_id_invalid",
                errorMessage = "owner id($id) does not represent a known owner"
            )

        return Response.Single(owner)

    }

    fun createNew(newOwner: NewOwnerDto): Response<*> {

        if (newOwner.firstName.isNullOrBlank() && newOwner.lastName.isNullOrBlank())
            return Response.Error(
                errorCode = "error_owner_name_invalid",
                errorMessage = "Owner needs at least a first name, or last name, or both"
            )

        if (newOwner.city.isBlank() || newOwner.city.isEmpty())
            return Response.Error(
                errorCode = "error_owner_city_invalid",
                errorMessage = "owner city(${newOwner.city}) can't be empty"
            )

        val matching =
            Op.build { PetOwner.Table.city eq newOwner.city }
                .andIfNotNull { newOwner.firstName?.let { Op.build { PetOwner.Table.firstName eq it } }  }
                .andIfNotNull { newOwner.lastName?.let { Op.build { PetOwner.Table.lastName eq it } } }

        val dto: OwnerDto.Simple =
            transaction {
                PetOwner.Table
                    .select(PetOwner.Table.id)
                    .where(matching)
                    .firstOrNull()?.get(PetOwner.Table.id)?.let { existingId ->
                        return@let Response.Error(
                            errorCode = "error_duplicate_owner",
                            errorMessage = "Duplicate owner($existingId) find in city ${newOwner.city} with ${
                                formattedName(
                                    newOwner.firstName,
                                    newOwner.lastName
                                )
                            }"
                        )
                    }

                PetOwner.new {
                    lastName = newOwner.lastName ?: ""
                    firstName = newOwner.firstName ?: ""
                    city = newOwner.city
                    telephone = newOwner.telephone
                    address = newOwner.address
                    pets = emptySized()
                }
            }.toDto()

        return Response.Single(dto)
    }

    private fun findOwnersCriteria(query: String?): Op<Boolean> =
        when {
            query.isNullOrBlank() -> Op.nullOp()
            query.isEmpty() -> Op.nullOp()
            else -> PetOwner.Table.lastName.like("%$query%")
                .or(PetOwner.Table.firstName.like("%$query%"))
        }

}

fun Pageable.location(): String =
    "page:$pageNo [$pageSize]"


private fun <T> SizedIterable<T>.limit(page: Pageable): SizedIterable<T> {
    page.slice().apply {
        limit(size, offset.toLong())
    }
    return this
}
