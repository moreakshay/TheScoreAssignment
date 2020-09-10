package com.moreakshay.thescoreassignment.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.moreakshay.thescoreassignment.utils.constants.*

@Entity(tableName = TEAM_TABLENAME, primaryKeys = [ID])
data class TeamEntity(
    @ColumnInfo(name = ID) val id: Int,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = WINS) val wins: Int,
    @ColumnInfo(name = LOSSES) val losses: Int,
)