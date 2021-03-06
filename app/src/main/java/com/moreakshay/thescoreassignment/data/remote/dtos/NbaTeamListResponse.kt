package com.moreakshay.thescoreassignment.data.remote.dtos


import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayersRelation
import com.moreakshay.thescoreassignment.utils.constants.EMPTY
import com.moreakshay.thescoreassignment.utils.constants.NOT_AVAILABLE
import com.moreakshay.thescoreassignment.utils.constants.NO_INT_AVAILABLE
import com.squareup.moshi.Json
import java.lang.IllegalArgumentException

data class NbaTeamListResponse(
    @field:Json(name = "full_name")
    val fullName: String,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "losses")
    val losses: Int?,
    @field:Json(name = "players")
    val players: List<Player?>?,
    @field:Json(name = "wins")
    val wins: Int?
) {
    data class Player(
        @field:Json(name = "first_name")
        val firstName: String?,
        @field:Json(name = "id")
        val id: Int,
        @field:Json(name = "last_name")
        val lastName: String?,
        @field:Json(name = "number")
        val number: Int?,
        @field:Json(name = "position")
        val position: String?
    )
}

fun NbaTeamListResponse.toTeamWithPlayers(): TeamWithPlayersRelation {
    val teamEntity = toTeamEntity()

    val roster = players?.map { it?.toEntity(teamEntity.id) }
        ?.takeIf { it.isNotEmpty() }
        ?: throw IllegalArgumentException("Players or Player values is null")

    return TeamWithPlayersRelation(team = teamEntity, players = roster.filterNotNull())
}

fun NbaTeamListResponse.toTeamEntity(): TeamEntity {
    return TeamEntity(
        id = id,
        name = fullName,
        wins = wins ?: NO_INT_AVAILABLE,
        losses = losses ?: NO_INT_AVAILABLE
    )
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

fun NbaTeamListResponse.createPlayerList(): List<PlayerEntity> {
    val players = mutableListOf<PlayerEntity>()
    this.players?.forEach { player ->
        if (player != null) { // this is needed because of nullable list items
            players.add(player.toEntity(this.id))
        }
    }
    return players
}