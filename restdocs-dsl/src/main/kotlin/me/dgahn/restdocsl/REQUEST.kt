package me.dgahn.restdocsl

import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.request.PathParametersSnippet
import org.springframework.restdocs.snippet.Snippet

open class REQUEST(
    var pathVariables: PathParametersSnippet? = null,
    var headers: RequestHeadersSnippet? = null,
    var requestBody: RequestFieldsSnippet? = null,
    override val consumer: DocumentConsumer<*>,
    override val builder: RestDocBuilder,
) : RestDocs

fun DOCUMENT.request(block: REQUEST.() -> Unit): Unit = REQUEST(
    consumer = consumer,
    builder = RequestBuilder(
        requestSpecification = this.requestSpecification,
        snippets = this.builder.snippets
    )
).visit(block)

class RequestBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>,
) : RestDocBuilder {
    override fun addSnippets(document: RestDocs) {
        val request = document as REQUEST

        request.pathVariables?.let { snippets.add(it) }
        request.headers?.let { snippets.add(it) }
        request.requestBody?.let { snippets.add(it) }
    }

    override fun build(document: RestDocs): Unit = Unit
}
