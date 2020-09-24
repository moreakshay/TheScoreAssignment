package com.moreakshay.thescoreassignment.utils

import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse

object TestUtils {
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
}