package com.moreakshay.thescoreassignment.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.utils.constants.TEAM_TABLENAME

@Dao
interface TeamDao: BaseDao<TeamEntity> {

    @Query("SELECT * FROM $TEAM_TABLENAME")
    suspend fun getAllTeams(): LiveData<List<TeamEntity>>
}