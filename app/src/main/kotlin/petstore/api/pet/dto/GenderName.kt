package petstore.api.pet.dto

@JvmInline
value class GenderName(private val value: String) : Comparable<GenderName> {
    override fun compareTo(other: GenderName) = value.compareTo(other.value)
}