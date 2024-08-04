package petstore.api.pet.dto

import java.time.LocalDate

data class Pet(
    val petId: String,
    val ownerId: String,
    val petName: String,
    val gender: GenderName,
    val type: PetType,
    val dateOfBirth: LocalDate?,
)