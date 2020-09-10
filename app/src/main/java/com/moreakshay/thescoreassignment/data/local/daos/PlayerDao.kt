package com.moreakshay.thescoreassignment.data.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.utils.constants.PLAYER_TABLENAME
import com.moreakshay.thescoreassignment.utils.constants.TEAM_ID

@Dao
interface PlayerDao: BaseDao<PlayerEntity> {

    @Query("SELECT * FROM $PLAYER_TABLENAME WHERE $TEAM_ID = :teamId")
    fun getAllPlayers(teamId: Int): LiveData<List<PlayerEntity>>
}