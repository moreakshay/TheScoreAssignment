package com.moreakshay.thescoreassignment.data.remote.dtos


import com.squareup.moshi.Json

data class NbaTeamListResponse(
    @Json(name = "full_name")
    val fullName: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "losses")
    val losses: Int?,
    @Json(name = "players")
    val players: List<Player?>?,
    @Json(name = "wins")
    val wins: Int?
) {
    data class Player(
        @Json(name = "first_name")
        val firstName: String?,
        @Json(name = "id")
        val id: Int?,
        @Json(name = "last_name")
        val lastName: String?,
        @Json(name = "number")
        val number: Int?,
        @Json(name = "position")
        val position: String?
    )
}
