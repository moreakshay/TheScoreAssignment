package com.moreakshay.thescoreassignment.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.utils.constants.LOSSES
import com.moreakshay.thescoreassignment.utils.constants.NAME
import com.moreakshay.thescoreassignment.utils.constants.TEAM_TABLENAME
import com.moreakshay.thescoreassignment.utils.constants.WINS

@Dao
interface TeamDao: BaseDao<TeamEntity> {

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $NAME ASC")
    fun getAllTeamsWithPlayers(): LiveData<List<TeamWithPlayers>>

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $WINS ASC")
    fun getAllTeamsWithPlayersSortByWins(): LiveData<List<TeamWithPlayers>>

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME ORDER BY $LOSSES ASC")
    fun getAllTeamsWithPlayersSortyByLosses(): LiveData<List<TeamWithPlayers>>
}