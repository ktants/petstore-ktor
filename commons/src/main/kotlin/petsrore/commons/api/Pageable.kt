package petsrore.commons.api

interface Pageable {

    val pageNo: Int
    val pageSize: Int

    private data class PageableImpl(override val pageNo: Int, override val pageSize: Int) : Pageable

    companion object {
        @JvmStatic
        fun of(pageNo: Int, pageSize: Int): Pageable = PageableImpl(pageNo, pageSize)
    }
}

fun Pageable.slice(): Slice = Slice.of(
    offset = (pageNo - 1) * pageSize,
    size = pageSize
)

fun Pageable.isEmpty(): Boolean = pageSize == 0
fun Pageable.isValid(): Boolean = pageNo > 0 && pageSize >= 0
