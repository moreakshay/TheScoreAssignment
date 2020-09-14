package com.moreakshay.thescoreassignment.utils

import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.toTeam
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.data.remote.dtos.toTeamWithPlayers
import org.mockito.ArgumentCaptor
import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

inline fun <reified T> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)

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

    fun getApiResponse() = listOf(
        NbaTeamListResponse(
            wins = 45,
            losses = 20,
            fullName = "Boston Celtics",
            id = 1,
            players = listOf(
                NbaTeamListResponse.Player(
                    id = 37729,
                    firstName = "Kadeem",
                    lastName = "Allen",
                    position = "SG",
                    number = 45,
                ),
                NbaTeamListResponse.Player(
                    id = 30508,
                    firstName = "Aron",
                    lastName = "Baynes",
                    position = "C",
                    number = 46,
                )
            )
        )
    )

    fun getTeamWithPlayerList() = getApiResponse().map { it.toTeamWithPlayers() }

    fun getTeamList() = getTeamWithPlayerList().map { it.toTeam() }
}