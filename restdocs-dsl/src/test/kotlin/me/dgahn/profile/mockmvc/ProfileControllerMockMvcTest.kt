package me.dgahn.profile.mockmvc

import io.kotest.core.spec.style.FunSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProfileControllerMockMvcTest(
    @Autowired mockMvc: MockMvc
) : FunSpec({
    test("post profile 호출") {
        mockMvc.perform(
            post("/v1/api/profiles")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andDo(
                document(
                    "create-profile",
                    requestFields(
                        fieldWithPath("id").description("식별자"),
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("access").description("접근"),
                    ),
                    responseFields(
                        fieldWithPath("id").description("식별자"),
                        fieldWithPath("name").description("이름"),
                        fieldWithPath("access").description("접근"),
                    )
                )
            )
    }
})
