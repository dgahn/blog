package me.dgahn.profile.util

import com.fasterxml.jackson.databind.ObjectMapper

val objectMapper = ObjectMapper()

fun <T> List<T>.toJson(): String = objectMapper.writeValueAsString(this)

fun <T> T.toJson(): String = objectMapper.writeValueAsString(this)
