package me.dgahn.profile.fixture

import me.dgahn.profile.ProfileController

val 기본_요청DTO = ProfileController.Request(
    id = 1L,
    name = "안덕기",
    access = ProfileController.ProfileAccess.PUBLIC,
)
