package petsrore.commons.api

interface Slice {
    val offset: Int
    val size: Int

    private data class SliceImpl(override val offset: Int, override val size: Int) : Slice

    companion object {
        @JvmStatic fun of(offset: Int, size: Int): Slice = SliceImpl(offset, size)
    }
}
