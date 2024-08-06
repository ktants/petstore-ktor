package petstore.api.owner

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.jetbrains.exposed.sql.SortOrder
import petstore.commons.api.Pageable
import petstore.commons.api.Response
import petstore.commons.jdbc.fetch
import petstore.api.owner.dto.OwnerDto
import petstore.common.testing.installExposedPlugin
import petstore.infrastructure.persistence.entity.PetOwner

class OwnerApiTest : DescribeSpec({
    val exposed = installExposedPlugin()
    val api = OwnerApi()

    it("reads owners") {
        exposed.get {
            PetOwner.Table.select(
                PetOwner.Table.id,
                PetOwner.Table.firstName,
                PetOwner.Table.lastName,
                PetOwner.Table.city
            ).orderBy(
                PetOwner.Table.lastName to SortOrder.ASC,
                PetOwner.Table.lastName to SortOrder.ASC,
            ).limit(1).execute(this)?.fetch {
                listOf(
                    getInt(1).toString(),
                    getString(2),
                    getString(3),
                    getString(4),
                )
            }
        }.shouldBeSuccess()
            .shouldNotBeNull()
            .shouldContainExactly(
                "7",
                "Jeff",
                "Black",
                "Monona"
            )
    }

    describe("fetching owners") {
        it("using defaults") {
            api.find(null, Pageable.of(1, 5))
                .shouldBeInstanceOf<Response.Many<List<OwnerDto>, *>>()
                .also {
                    assertSoftly(it) {
                        items.shouldNotBeEmpty()
                        hasMore shouldBe true
                    }
                }
        }
    }
})
