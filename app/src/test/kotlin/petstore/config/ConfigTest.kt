package petstore.config

import com.sksamuel.hoplite.ExperimentalHoplite
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec

@ExperimentalHoplite
class ConfigTest : DescribeSpec({
    describe("load configuration") {
        it("database configuration") {
            val c = shouldNotThrowAny { Config.load() }
            println(c)
        }
    }
})
