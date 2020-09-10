package com.moreakshay.thescoreassignment.data.remote.dtos


import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.utils.constants.EMPTY
import com.moreakshay.thescoreassignment.utils.constants.NOT_AVAILABLE
import com.moreakshay.thescoreassignment.utils.constants.NO_INT_AVAILABLE
import com.squareup.moshi.Json
import java.lang.IllegalArgumentException

data class NbaTeamListResponse(
    @field:Json(name = "full_name")
    val fullName: String,
    @Json(name = "id")
    val id: Int,
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
        val id: Int,
        @Json(name = "last_name")
        val lastName: String?,
        @Json(name = "number")
        val number: Int?,
        @Json(name = "position")
        val position: String?
    )
}

fun NbaTeamListResponse.toEntity(): TeamWithPlayers {
    val teamEntity = toTeamEntity()
    val roster = players?.map { it?.toEntity(teamEntity.id) }
    if (roster != null) {
        return TeamWithPlayers(team = teamEntity, players = roster.requireNoNulls())
    }
    throw IllegalArgumentException("Players or Player values is null")
}

fun NbaTeamListResponse.toTeamEntity(): TeamEntity {
    return TeamEntity(id = id,
        name = fullName ?: NOT_AVAILABLE,
        wins = wins ?: NO_INT_AVAILABLE,
        losses = losses ?: NO_INT_AVAILABLE)
}

fun NbaTeamListResponse.Player.toEntity(teamId: Int): PlayerEntity {
    return PlayerEntity(
        id = id,
        teamId = teamId,
        firstName = firstName ?: EMPTY,
        lastName = lastName ?: EMPTY,
        number = number ?: 0,
        position = position ?: NOT_AVAILABLE
    )
}