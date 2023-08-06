package me.dgahn.restdocsl

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.RequestHeadersSnippet

fun REQUEST.requestHeaders(vararg descriptors: HeaderDescriptor): RequestHeadersSnippet {
    return HeaderDocumentation.requestHeaders(listOf(*descriptors))
}

infix fun String.meanHeader(description: String): HeaderDescriptor {
    return HeaderDocumentation.headerWithName(this).description(description)
}
