package me.dgahn.restdocsl

import io.restassured.specification.RequestSpecification
import org.springframework.restdocs.snippet.Snippet

interface RestDocBuilder {
    val requestSpecification: RequestSpecification
    val snippets: MutableList<Snippet>

    fun addSnippets(document: RestDocs)
    fun build(document: RestDocs)
}

interface DocumentConsumer<out R> {
    val requestSpecification: RequestSpecification

    fun onDocumentStart(restDocs: RestDocs)
    fun onDocumentEnd(restDocs: RestDocs)
    fun onDocumentError(restDocs: RestDocs, exception: Throwable): Unit = throw exception
    fun finalize(): R
}

@Suppress("TooGenericExceptionCaught")
fun <T : RestDocs> T.visit(block: T.() -> Unit) {
    consumer.onDocumentStart(this)
    try {
        this.block()
    } catch (err: Throwable) {
        consumer.onDocumentError(this, err)
    } finally {
        consumer.onDocumentEnd(this)
    }
}

fun <T : RestDocs, R> T.visitAndFinalize(consumer: DocumentConsumer<R>, block: T.() -> Unit): R {
    if (this.consumer !== consumer) {
        throw IllegalArgumentException("Wrong exception")
    }

    visit(block)
    return consumer.finalize()
}

class DocumentStreamBuilder<O : RequestSpecification>(
    override val requestSpecification: O
) : DocumentConsumer<O> {

    override fun onDocumentStart(restDocs: RestDocs) {
        restDocs.builder.addSnippets(restDocs)
    }

    override fun onDocumentEnd(restDocs: RestDocs) {
        restDocs.builder.build(restDocs)
    }

    override fun finalize(): O = requestSpecification
}
