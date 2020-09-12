package com.moreakshay.thescoreassignment.utils

import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team

object TestUtils {

    fun createTeam(id: Int, name: String, wins: Int, losses: Int): TeamEntity = TeamEntity(
        id = id,
        name = name,
        wins = wins,
        losses = losses
    )

    fun createPlayer(
        id: Int, teamId: Int, firstName: String, lastName: String, position: String,
        number: Int
    ): PlayerEntity = PlayerEntity(
        id = id,
        teamId = teamId,
        firstName = firstName,
        lastName = lastName,
        position = position,
        number = number
    )

    fun createPlayers(count: Int): List<PlayerEntity> {
        return (0 until count).map {
            PlayerEntity(
                id = it,
                teamId = it + 10,
                firstName = "$it" + "first",
                lastName = "last$it",
                position = "POS",
                number = it * 10
            )
        }
    }

}