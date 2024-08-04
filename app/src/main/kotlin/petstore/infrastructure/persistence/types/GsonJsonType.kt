package petstore.infrastructure.persistence.types

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect

class GsonJsonType(private val gson: Gson) : ColumnType<JsonElement>() {

    override fun sqlType(): String = currentDialect.dataTypeProvider.jsonType()

    override fun valueFromDB(value: Any): JsonElement? = when (value) {
        is JsonElement -> value
        is String -> gson.fromJson(value, JsonElement::class.java)
        is ByteArray -> gson.fromJson(value.decodeToString(), JsonElement::class.java)
        else -> null
    }
}

private val gson = GsonBuilder().create()

fun Table.json(name: String, gson: Gson): Column<JsonElement> =
    registerColumn(name, GsonJsonType(gson))

fun Table.json(name: String): Column<JsonElement> =
    json(name, gson)

