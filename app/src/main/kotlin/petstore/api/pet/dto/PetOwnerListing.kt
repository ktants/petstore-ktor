package petstore.api.pet.dto

class PetOwnerListing(
    val id: String?,
    val firstName: String,
    val lastName: String,
    val city: String,
    val pets: List<String>,
)
