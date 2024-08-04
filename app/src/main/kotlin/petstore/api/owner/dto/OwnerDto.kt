package petstore.api.owner.dto

import com.fasterxml.jackson.annotation.JsonInclude


sealed interface OwnerDto {

    val id: Any?
    val name: String

    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Listing(
        override val id: Any?,
        override val name: String,
        val address: String,
        val city: String,
        val telephone: String,
        val pets: List<String>,
    ) : OwnerDto


    data class Simple(
        override val id: Any,
        override val name: String,
        val address: String,
        val city: String,
        val telephone: String,
    ) : OwnerDto
}