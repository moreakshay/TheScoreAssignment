package com.moreakshay.thescoreassignment.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moreakshay.thescoreassignment.data.local.daos.PlayerDao
import com.moreakshay.thescoreassignment.data.local.daos.TeamDao
import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity

@Database(entities = [TeamEntity::class, PlayerEntity::class], version = 1)
abstract class TheScoreDatabase: RoomDatabase() {
    abstract fun teamDao(): TeamDao
    abstract fun playerDao(): PlayerDao
//    abstract fun teamWithPlayersDao(): TeamWithPlayersDao
}