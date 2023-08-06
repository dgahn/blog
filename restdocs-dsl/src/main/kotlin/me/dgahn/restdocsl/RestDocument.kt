package me.dgahn.restdocsl

import io.restassured.RestAssured
import io.restassured.http.Method
import io.restassured.specification.RequestSpecification
import org.springframework.http.HttpStatus
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation
import org.springframework.restdocs.snippet.Snippet

interface RestDocs {
    val consumer: DocumentConsumer<*>
    val builder: RestDocBuilder
}

open class DOCUMENT(
    var statusCode: HttpStatus = HttpStatus.OK,
    var url: String? = null,
    var method: Method? = null,
    var requestBodyValue: String? = null,
    var pathVariables: List<String> = emptyList(),
    val identifier: String,
    val requestSpecification: RequestSpecification,
    override val consumer: DocumentConsumer<*>,
    override val builder: RestDocBuilder,
) : RestDocs

open class DocumentBuilder(
    override val requestSpecification: RequestSpecification,
    override val snippets: MutableList<Snippet>
) : RestDocBuilder {
    override fun addSnippets(document: RestDocs): Unit = Unit
    override fun build(document: RestDocs) {
        val doc = document as DOCUMENT
        RestAssured.given(requestSpecification)
            .contentType("application/json")
            .accept("application/json")
            .apply {
                if (doc.requestBodyValue != null) {
                    this.body(doc.requestBodyValue)
                }
            }
            .filter(
                RestAssuredRestDocumentation.document(
                    doc.identifier,
                    *snippets.toTypedArray()
                )
            )
            .`when`()
            .request(doc.method, doc.url, *doc.pathVariables.toTypedArray())
            .then()
            .assertThat()
            .statusCode(doc.statusCode.value())
    }
}

fun <O : RequestSpecification> O.makeDocument(identifier: String, block: DOCUMENT.() -> Unit): O {
    val consumer = DocumentStreamBuilder(this)
    return DOCUMENT(
        identifier = identifier,
        requestSpecification = this,
        consumer = consumer,
        builder = DocumentBuilder(requestSpecification = request(), snippets = mutableListOf())
    ).visitAndFinalize(consumer, block)
}
