package me.dgahn.profile

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfileController {
    data class Request(
        val name: String,
        val phoneNos: List<String>,
        val profileVisibility: ProfileVisibility,
    )

    data class Response(
        val name: String,
        val phoneNos: List<String>,
        val profileVisibility: ProfileVisibility,
    )

    enum class ProfileVisibility {
        PUBLIC,
        PRIVATE;
    }

    @PostMapping("/profiles")
    fun post(@RequestBody request: Request): ResponseEntity<Response> {
        return ResponseEntity.ok(
            Response(
                name = request.name,
                phoneNos = request.phoneNos,
                profileVisibility = request.profileVisibility
            )
        )
    }

    @PostMapping("/profiles/{name}")
    fun post(@RequestBody request: Request, @PathVariable name: String): ResponseEntity<Response> {
        return ResponseEntity.ok(
            Response(
                name = request.name,
                phoneNos = request.phoneNos,
                profileVisibility = request.profileVisibility
            )
        )
    }

    @GetMapping("/profiles")
    fun get(): ResponseEntity<Response> {
        return ResponseEntity.ok(
            Response(
                name = "name",
                phoneNos = listOf("PhoneNumber"),
                profileVisibility = ProfileVisibility.PUBLIC
            )
        )
    }

    @GetMapping("/profiles/{name}")
    fun getByName(@PathVariable name: String): ResponseEntity<Response> {
        return ResponseEntity.ok(
            Response(
                name = name,
                phoneNos = listOf("PhoneNumber"),
                profileVisibility = ProfileVisibility.PUBLIC
            )
        )
    }
}
