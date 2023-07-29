package me.dgahn.profile

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ProfileController {
    data class Request(
        val id: Long,
        val name: String,
        val img: MultipartFile,
        val access: ProfileAccess
    ) {
        fun toResponse(): Response {
            return Response(
                id = id,
                name = name,
                img = img,
                access = access
            )
        }
    }

    data class Response(
        val id: Long,
        val name: String,
        val img: MultipartFile,
        val access: ProfileAccess
    )

    enum class ProfileAccess {
        PUBLIC,
        PRIVATE,
        FRIENDS_ONLY
    }

    @PostMapping("/v1/api/profiles")
    fun post(request: Request): ResponseEntity<Response> {
        return ResponseEntity.ok(request.toResponse())
    }
}