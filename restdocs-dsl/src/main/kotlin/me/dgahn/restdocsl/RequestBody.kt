package me.dgahn.restdocsl

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet

open class RequestBody(override val consumer: DocumentConsumer<*>, override val builder: RestDocBuilder) : RestDocs

fun REQUEST.requestBody(vararg descriptors: FieldDescriptor): RequestFieldsSnippet {
    return PayloadDocumentation.requestFields(listOf(*descriptors))
}
