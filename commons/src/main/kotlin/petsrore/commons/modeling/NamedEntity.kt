package petsrore.commons.modeling

open class NamedEntity<T> : BaseEntity<T>() {

    open var name: String? = null

    override fun toString(): String {
        return name ?: ""
    }

}