@file:JvmName("Jdbc")

package petsrore.commons.jdbc

import io.ktor.http.*
import java.io.IOException
import java.net.URL
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


@Throws(SQLException::class, IOException::class)
fun DataSource.executeScript(script: URL) =
    transaction {
        createStatement().use { statement ->
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