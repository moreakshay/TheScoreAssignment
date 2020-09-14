package com.moreakshay.thescoreassignment.data.local.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg obj: T) : Array<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<T>) : List<Long>

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}