package me.dgahn.restdocsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.ResponseFieldsSnippet

open class ResponseBody(override val consumer: DocumentConsumer<*>, override val builder: RestDocBuilder) : RestDocs

fun RESPONSE.responseBody(vararg descriptors: FieldDescriptor): ResponseFieldsSnippet {
    return PayloadDocumentation.responseFields(listOf(*descriptors))
}
