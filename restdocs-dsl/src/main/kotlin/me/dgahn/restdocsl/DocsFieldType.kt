package me.dgahn.restdocsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation

sealed class DocsFieldType(
    val type: JsonFieldType
)

object ARRAY : DocsFieldType(JsonFieldType.ARRAY)
object BOOLEAN : DocsFieldType(JsonFieldType.BOOLEAN)
object OBJECT : DocsFieldType(JsonFieldType.OBJECT)
object NUMBER : DocsFieldType(JsonFieldType.NUMBER)
object NULL : DocsFieldType(JsonFieldType.NULL)
object STRING : DocsFieldType(JsonFieldType.STRING)
object ANY : DocsFieldType(JsonFieldType.VARIES)
object DATE : DocsFieldType(JsonFieldType.STRING)
object DATETIME : DocsFieldType(JsonFieldType.STRING)

infix fun FieldDescriptor.meanBody(description: String): FieldDescriptor {
    return this.description(description)
}

infix fun String.type(
    docsFieldType: DocsFieldType
): FieldDescriptor {
    return PayloadDocumentation.fieldWithPath(this).type(docsFieldType.type)
}
