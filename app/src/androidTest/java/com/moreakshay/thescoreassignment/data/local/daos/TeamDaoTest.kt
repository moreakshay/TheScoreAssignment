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
import com.squareup.moshi.Types
import getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.assertj.core.api.Assertions.assertThat


@RunWith(AndroidJUnit4::class)
class TeamDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertTeamDao_read_returnTrue() {
        val teamEntity = TestUtils.createTeam(1, "Boston Celtics", 45, 20)
        db.teamDao().insert(teamEntity)
        val loaded = db.teamDao().getTeamById(1).getOrAwaitValue()
        assertThat(loaded).isNotNull()
        assertThat(loaded.id).isEqualTo(1)
        assertThat(loaded.name).isEqualTo("Boston Celtics")
        assertThat(loaded.wins).isEqualTo(45)
        assertThat(loaded.losses).isEqualTo(20)
    }

    @Test
    fun getAllTeamsWithPlayers_sortyAlphabetically_returnTrue() {
        //GIVEN insert values
        val loadedData: List<NbaTeamListResponse> =
            runBlocking {
                val type =
                    Types.newParameterizedType(List::class.java, NbaTeamListResponse::class.java)

                Moshi.Builder().build()
                    .adapter<List<NbaTeamListResponse>>(type).fromJson(
                        MockResponseFileReader("TeamResponse.json").content
                    ) ?: emptyList()
            }
        loadedData.forEach { teamListResponse ->
            db.teamDao().insert(teamListResponse.toTeamEntity())
            db.playerDao().insertAll(teamListResponse.createPlayerList())
        }

        val list = loadedData.map { it.toEntity() }
            .sortedBy { it.team.name }

        //WHEN values sorted alphabetically
        val teams: List<TeamWithPlayers> = db.teamDao().getAllTeamsWithPlayers().getOrAwaitValue()

        //THEN
        assertThat(teams.map { it.team.name }).containsExactlyElementsOf(list.map { it.team.name })
    }
}