package com.moreakshay.thescoreassignment.data.local.entities

import androidx.lifecycle.*
import androidx.room.Embedded
import androidx.room.Relation
import com.moreakshay.thescoreassignment.core.dispatchers.DispatcherProvider
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.constants.ID
import com.moreakshay.thescoreassignment.utils.constants.TEAM_ID

data class TeamWithPlayersRelation(
    @Embedded val team: TeamEntity,
    @Relation(
        parentColumn = ID,
        entityColumn = TEAM_ID
    ) val players: List<PlayerEntity>
)

fun LiveData<List<TeamWithPlayersRelation>>.toPlayer(): LiveData<List<Team>> = switchMap { list ->
    liveData(DispatcherProvider.IO) {
        emit(list.map { it.toTeam() })
    }
}

fun TeamWithPlayersRelation.toTeam(): Team {
    return Team(
        id = team.id,
        name = team.name,
        wins = team.wins,
        losses = team.losses,
        players = players.map { it.toPlayer() }
    )
}

fun PlayerEntity.toPlayer(): Team.Player {
    return Team.Player(
        id = id,
        firstName = firstName,
        lastName = lastName,
        position = position,
        number = number
    )
}