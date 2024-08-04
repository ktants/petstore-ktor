@file:JvmName("Jdbc")

package petsrore.commons.jdbc

import io.ktor.http.*
import petsrore.commons.ScriptBlock
import petsrore.commons.scriptBlocks
import java.net.URL
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


@Suppress("SqlSourceToSinkFlow")
@Throws(SQLException::class)
fun DataSource.executeScript(script: URL) =
    transaction {
        val statement = createStatement()
        script.openStream().bufferedReader().scriptBlocks {
            statement.execute(content)
            ScriptBlock.ProcessControl.CONTINUE
        }
    }


@Throws(SQLException::class)
fun DataSource.connection(): Connection = this.connection

@Throws(SQLException::class)
fun DataSource.transaction(action: Connection.() -> Unit) {
    connection().use { c ->
        val isAutoCommit = c.autoCommit
        val r = c.runCatching(action)
        try {
            r.onFailure { c.rollback() }
            r.onSuccess { c.commit() }
        } finally {
            c.autoCommit = isAutoCommit
        }
    }
}