package petstore.common.testing

import io.kotest.core.TestConfiguration
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.IOException
import java.util.Properties

sealed interface ExposedTestPlugin {

    fun <R> get(retrieve: Transaction.() -> R): Result<R>

    operator fun invoke(action: Transaction.() -> Unit): Throwable?

    data class Config(
        val jdbcUrl: String,
        val jdbcUsername: String,
        val jdbcPassword: String? = null,
        val jdbcDriver: String? = null,
        val exposedConfig: (DatabaseConfig.Builder.() -> Unit)? = null,
    )

    companion object {
        const val CLASSPATH_PROPERTIES = "/expose-test-plugin.properties"
    }
}


fun TestConfiguration.installExposedPlugin(): ExposedTestPlugin {
    return installExposedPlugin(
        TestConfiguration::class.java.getResource(ExposedTestPlugin.CLASSPATH_PROPERTIES)
            ?.openStream()?.reader()?.use { Properties().apply { load(it) } }
            ?.run {
                ExposedTestPlugin.Config(
                    jdbcUrl = requireNotNull(getProperty("exposed.jdbc.url")) { "exposed.jdbc.url must be set" },
                    jdbcUsername = requireNotNull(getProperty("exposed.jdbc.username")) { "exposed.jdbc.username must be set" },
                    jdbcPassword = getProperty("exposed.jdbc.password") ?: "",
                    jdbcDriver = requireNotNull(getProperty("exposed.jdbc.driver")) { "exposed.jdbc.driver not set" },
                ) {
                    getProperty("exposed.jdbc.schema")?.let(::Schema)?.also { defaultSchema = it }
                }
            } ?: throw IOException(ExposedTestPlugin.CLASSPATH_PROPERTIES)
    )
}

fun TestConfiguration.installExposedPlugin(
    config: ExposedTestPlugin.Config,
): ExposedTestPlugin = ExposedTestPluginImpl(this, config)

private class ExposedTestPluginImpl(
    testing: TestConfiguration,
    private val params: ExposedTestPlugin.Config,
) : ExposedTestPlugin {

    private var _db: Database? = null

    private val db: Database
        get() = _db ?: throw IllegalStateException("Exposed database connection is not available outside of spec!")

    init {
        testing.listeners(object : TestListener {
            override suspend fun beforeSpec(spec: Spec) {
                installExposedContext()
            }

            override suspend fun afterSpec(spec: Spec) {
                releaseExposedContext()
            }
        })
    }

    private fun releaseExposedContext() {
        _db?.also { TransactionManager.closeAndUnregister(it) }
    }

    private fun installExposedContext() {
        releaseExposedContext()
        _db = Database.connect(
            url = params.jdbcUrl,
            driver = params.jdbcDriver ?: "",
            user = params.jdbcUsername,
            password = params.jdbcPassword ?: "",
            databaseConfig = params.exposedConfig?.let(DatabaseConfig::invoke)
        )
    }

    override fun invoke(action: Transaction.() -> Unit): Throwable? =
        get { action() }.exceptionOrNull()

    override fun <R> get(retrieve: Transaction.() -> R): Result<R> =
        runCatching { transaction(db, retrieve) }

}