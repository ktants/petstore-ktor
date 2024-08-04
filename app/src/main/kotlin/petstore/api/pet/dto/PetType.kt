package petstore.api.pet.dto

@JvmInline
value class PetType(private val value: String): Comparable<PetType> {
    override fun compareTo(other: PetType): Int = value.compareTo(other.value)
}
