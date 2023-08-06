package me.dgahn.restdocsl

import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.ResponseHeadersSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet

open class RESPONSE(
    var headers: ResponseHeadersSnippet? = null,
    var responseBody: ResponseFieldsSnippet? = null,
    override val consumer: DocumentConsumer<*>,
    override val builder: RestDocBuilder,
) : RestDocs

fun DOCUMENT.response(block: RESPONSE.() -> Unit) {
    return RESPONSE(
        consumer = consumer,
        builder = ResponseBuilder(
            requestSpecification = this.requestSpecification,
            snippets = this.builder.snippets
        )
    ).visit(block)
}

open class ResponseBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>
) : RestDocBuilder {
    override fun addSnippets(document: RestDocs) {
        val response = document as RESPONSE

        response.headers?.let { snippets.add(it) }
        response.responseBody?.let { snippets.add(it) }
    }

    override fun build(document: RestDocs): Unit = Unit
}
