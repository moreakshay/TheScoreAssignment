package com.moreakshay.thescoreassignment.data.local.entities

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Embedded
import androidx.room.Relation
import com.moreakshay.thescoreassignment.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.ID
import com.moreakshay.thescoreassignment.utils.constants.TEAM_ID

data class TeamWithPlayers(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = ID,
        entityColumn = TEAM_ID
    ) val players: List<PlayerEntity>
)

fun LiveData<List<TeamWithPlayers>>.toDomainModel(): LiveData<List<Team>> {
    return Transformations.map(this){ it.map { it.toDomainModel() }}
}

fun TeamWithPlayers.toDomainModel(): Team {
    return Team(
        id = team.id,
        name = team.name,
        wins = team.wins,
        losses = team.losses,
        players = players.map { it.toDomainModel() }
    )
}

fun PlayerEntity.toDomainModel(): Team.Player {
    return Team.Player(
        id = id,
        firstName = firstName,
        lastName = lastName,
        position = position,
        number = number
    )
}