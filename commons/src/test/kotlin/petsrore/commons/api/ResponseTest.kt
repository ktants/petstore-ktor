package petsrore.commons.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.DescribeSpec
import java.time.LocalDate

class ResponseTest : DescribeSpec({
    describe("json serialization") {

        val mapper = jacksonObjectMapper().findAndRegisterModules()

        describe("successful response") {
            it("should serialize successful response") {
                val part: Response.Part<*, String> = Response.Part(
                    data = mapOf(
                        "name" to "peter",
                        "age" to 21,
                        "today" to LocalDate.of(2024, 1, 16)
                    ),
                    location = "location",
                    hasMore = true
                )

                val json = mapper.writeValueAsString(part)
                println(json)
            }
        }

    }
})
