package petstore.api.pet.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("owner")
class PetOwnerListing(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val city: String,
    val pets: List<String>,
)
