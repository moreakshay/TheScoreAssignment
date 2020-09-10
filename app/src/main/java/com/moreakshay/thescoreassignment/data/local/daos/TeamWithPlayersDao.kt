package com.moreakshay.thescoreassignment.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.utils.constants.TEAM_TABLENAME

/*
@Dao
interface TeamWithPlayersDao: BaseDao<TeamWithPlayers> {

    @Transaction
    @Query("SELECT * FROM $TEAM_TABLENAME")
    fun getAllTeamsWithPlayers(): LiveData<List<TeamWithPlayers>>
}*/
