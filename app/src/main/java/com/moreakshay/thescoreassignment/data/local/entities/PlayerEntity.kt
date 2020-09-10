package com.moreakshay.thescoreassignment.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.moreakshay.thescoreassignment.utils.constants.*

@Entity(tableName = PLAYER_TABLENAME, primaryKeys = [PLAYER_ID])
data class PlayerEntity(
    @ColumnInfo(name = PLAYER_ID) val id: Int,
    @ColumnInfo(name = TEAM_ID) val teamId: Int,
    @ColumnInfo(name = FIRST_NAME) val firstName: String,
    @ColumnInfo(name = LAST_NAME) val lastName: String,
    @ColumnInfo(name = POSITION) val position: String,
    @ColumnInfo(name = NUMBER) val number: Int,
)