package com.moreakshay.thescoreassignment.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayersRelation
import com.moreakshay.thescoreassignment.utils.constants.*

@Dao
interface TeamDao: BaseDao<TeamEntity> {

    @Query("SELECT * FROM $TEAM_TABLENAME WHERE $ID = :id")
    fun getTeamById(id: Int): LiveData<TeamEntity>

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $NAME ASC")
    fun getAllTeamsWithPlayers(): LiveData<List<TeamWithPlayersRelation>>

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $WINS DESC")
    fun getAllTeamsWithPlayersSortByWins(): LiveData<List<TeamWithPlayersRelation>>

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $LOSSES DESC")
    fun getAllTeamsWithPlayersSortedByLosses(): LiveData<List<TeamWithPlayersRelation>>
}