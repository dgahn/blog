package me.dgahn.profile

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController {
    data class Request(
        val id: Long,
        val name: String,
        val access: ProfileAccess
    ) {
        fun toResponse(): Response {
            return Response(
                id = id,
                name = name,
                access = access
            )
        }
    }

    data class Response(
        val id: Long,
        val name: String,
        val access: ProfileAccess
    )

    enum class ProfileAccess {
        PUBLIC,
        PRIVATE,
        FRIENDS_ONLY
    }

    @PostMapping("/v1/api/profiles")
    fun post(@RequestBody request: Request): ResponseEntity<Response> {
        return ResponseEntity.ok(request.toResponse())
    }
}
