package me.dgahn.restdocsl

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.ResponseHeadersSnippet

fun RESPONSE.responseHeaders(vararg descriptors: HeaderDescriptor): ResponseHeadersSnippet {
    return HeaderDocumentation.responseHeaders(listOf(*descriptors))
}
