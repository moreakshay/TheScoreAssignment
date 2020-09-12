package com.moreakshay.thescoreassignment.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.moreakshay.thescoreassignment.data.local.DbTest
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.data.remote.dtos.createPlayerList
import com.moreakshay.thescoreassignment.data.remote.dtos.toEntity
import com.moreakshay.thescoreassignment.data.remote.dtos.toTeamEntity
import com.moreakshay.thescoreassignment.utils.MockResponseFileReader
import com.moreakshay.thescoreassignment.utils.TestUtils
import com.squareup.moshi.Moshi
import getOrAwaitValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is` as isEqualTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeamDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertTeamDao_read_returnTrue() {
        val teamEntity = TestUtils.createTeam(1, "Boston Celtics", 45, 20)
        db.teamDao().insert(teamEntity)
        val loaded = db.teamDao().getTeamById(1).getOrAwaitValue()
        assertThat(loaded, notNullValue())
        assertThat(loaded.id, isEqualTo(1))
        assertThat(loaded.name, isEqualTo("Boston Celtics"))
        assertThat(loaded.wins, isEqualTo(45))
        assertThat(loaded.losses, isEqualTo(20))
    }

    @Test
    fun getAllTeamsWithPlayers_sortyAlphabetically_returnTrue(){
        //GIVEN insert values
        var list: List<NbaTeamListResponse> =
        runBlocking {
            Moshi.Builder().build().adapter<List<NbaTeamListResponse>>(NbaTeamListResponse::class.java).fromJson(
                MockResponseFileReader("SmallTeamResponse.json").content
            ) ?: emptyList()
        }
        list.forEach { teamListResponse ->
            db.teamDao().insert(teamListResponse.toTeamEntity())
            db.playerDao().insertAll(teamListResponse.createPlayerList())
        }
        list.sortedBy { it.fullName }
        list.map { it.toEntity() }

        //WHEN values sorted alphabetically
        val teams: List<TeamWithPlayers> = db.teamDao().getAllTeamsWithPlayers().getOrAwaitValue()

        //THEN
        assertThat(teams, IsIterableContainingInOrder.contains(*(list.toTypedArray())))
    }

}