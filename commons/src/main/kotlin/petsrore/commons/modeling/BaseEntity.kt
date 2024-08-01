package petsrore.commons.modeling

import java.util.Objects

open class BaseEntity<T> {

    open var id: T? = null
    val isNew: Boolean get() = id == null

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other !is BaseEntity<*> -> false
            else -> id == other.id
        }
    }

    override fun hashCode(): Int = Objects.hash(id)

}

