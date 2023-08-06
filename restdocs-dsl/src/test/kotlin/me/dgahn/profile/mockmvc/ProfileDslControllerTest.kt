package me.dgahn.profile.mockmvc

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.http.Method
import me.dgahn.profile.ProfileController
import me.dgahn.restdocsl.NUMBER
import me.dgahn.restdocsl.STRING
import me.dgahn.restdocsl.makeDocument
import me.dgahn.restdocsl.meanBody
import me.dgahn.restdocsl.meanHeader
import me.dgahn.restdocsl.meanPath
import me.dgahn.restdocsl.pathVariables
import me.dgahn.restdocsl.request
import me.dgahn.restdocsl.requestBody
import me.dgahn.restdocsl.requestHeaders
import me.dgahn.restdocsl.response
import me.dgahn.restdocsl.responseBody
import me.dgahn.restdocsl.responseHeaders
import me.dgahn.restdocsl.type
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(RestDocumentationExtension::class)
class ProfileDslControllerTest(
    @LocalServerPort port: Int
) : AbsctracControllerTest(port) {

    @Test
    fun `프로필_등록`() {
        this.spec.makeDocument(identifier = "index2") {
            url = "/profiles/{name}"
            method = Method.POST
            pathVariables = listOf("name")
            statusCode = HttpStatus.OK
            requestBodyValue = request.toJson()
            request {
                pathVariables = pathVariables(
                    "name" meanPath "이름",
                )
                headers = requestHeaders(
                    "Authorization" meanHeader "인증"
                )
                requestBody = requestBody(
                    "name" type STRING meanBody "이름",
                    "age" type NUMBER meanBody "10"
                )
            }
            response {
                headers = responseHeaders(
                    "Authorization" meanHeader "인증"
                )
                responseBody = responseBody(
                    "name" type STRING meanBody "이름",
                    "age" type NUMBER meanBody "10"
                )
            }
        }
    }
}

val request = ProfileController.Request(
    name = "이름",
    phoneNos = listOf(),
    profileVisibility = ProfileController.ProfileVisibility.PUBLIC
)

val objectMapper = ObjectMapper()

fun <T> T.toJson(): String = objectMapper.writeValueAsString(this)
