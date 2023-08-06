package me.dgahn.profile.mockmvc

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.BeforeEach
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration

@Suppress("UnnecessaryAbstractClass")
abstract class AbsctracControllerTest(
    private val port: Int
) {
    protected lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider?) {
        this.spec = RequestSpecBuilder()
            .setPort(port)
            .addFilter(documentationConfiguration(restDocumentation))
            .build()
    }
}
