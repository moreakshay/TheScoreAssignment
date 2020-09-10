package com.moreakshay.thescoreassignment.teamlist

import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse

data class Team(val id: Int,
val name: String,
val wins: Int,
val losses: Int,
val players: List<Player>){
    data class Player(val id: Int,
    val firstName: String,
    val lastName: String,
    val position: String,
    val number: Int)
}
