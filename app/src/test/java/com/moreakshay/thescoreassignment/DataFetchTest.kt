package com.moreakshay.thescoreassignment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.moreakshay.thescoreassignment.data.TheScoreRepository
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.local.daos.PlayerDao
import com.moreakshay.thescoreassignment.data.local.daos.TeamDao
import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.utils.network.Status
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when` as whenever
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class DataFetchTest {
    @field:Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val moshi: Moshi = Moshi.Builder().build()

    @Suppress("BlockingMethodInNonBlockingContext")
    private val fakeApiService = object : ApiService {
        override suspend fun getNbaTeamList(): List<NbaTeamListResponse> =
            runBlocking {
                moshi.adapter<List<NbaTeamListResponse>>(NbaTeamListResponse::class.java).fromJson(
                    SMALL_API_RESPONSE_DATA
                ) ?: emptyList()
            }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun checkApiRequestSuccessSavesDataCorrectly() = runBlockingTest {
        val mockDatabase = mock(TheScoreDatabase::class.java)

        whenever(mockDatabase.playerDao()).thenReturn(object: PlayerDao {
            override fun getAllPlayers(teamId: Int): LiveData<List<PlayerEntity>> {
                TODO("Not yet implemented")
            }

            override fun insert(obj: PlayerEntity): Long {
                TODO("Not yet implemented")
            }

            override fun insertAll(vararg obj: PlayerEntity): Array<Long> {
                TODO("Not yet implemented")
            }

            override fun insertAll(list: List<PlayerEntity>): List<Long> {
                TODO("Not yet implemented")
            }

            override fun update(obj: PlayerEntity) {
                TODO("Not yet implemented")
            }

            override fun delete(obj: PlayerEntity) {
                TODO("Not yet implemented")
            }
        })
        whenever(mockDatabase.teamDao()).thenReturn(object: TeamDao {
            override suspend fun getAllTeamsOnce(): List<TeamWithPlayers> {
                TODO("Not yet implemented")
            }

            override fun getAllTeamsWithPlayers(): LiveData<List<TeamWithPlayers>> {
                TODO("Not yet implemented")
            }

            override fun getAllTeamsWithPlayersSortByWins(): LiveData<List<TeamWithPlayers>> {
                TODO("Not yet implemented")
            }

            override fun getAllTeamsWithPlayersSortyByLosses(): LiveData<List<TeamWithPlayers>> {
                TODO("Not yet implemented")
            }

            override fun insert(obj: TeamEntity): Long {
                TODO("Not yet implemented")
            }

            override fun insertAll(vararg obj: TeamEntity): Array<Long> {
                TODO("Not yet implemented")
            }

            override fun insertAll(list: List<TeamEntity>): List<Long> {
                TODO("Not yet implemented")
            }

            override fun update(obj: TeamEntity) {
                TODO("Not yet implemented")
            }

            override fun delete(obj: TeamEntity) {
                TODO("Not yet implemented")
            }
        })

        val repository = TheScoreRepository(
            mockDatabase,
            fakeApiService
        )

        val response = listOf(
            NbaTeamListResponse(
                wins = 45,
                losses = 20,
                fullName = "Boston Celtics",
                id = 1,
                players = listOf(
                    NbaTeamListResponse.Player(
                        id = 37729,
                        firstName = "Kadeem",
                        lastName = "Allen",
                        position = "SG",
                        number = 45,
                    ),
                    NbaTeamListResponse.Player(
                        id = 30508,
                        firstName = "Aron",
                        lastName = "Baynes",
                        position = "C",
                        number = 46,
                    )
                )
            )
        )

        val teams = repository.getAllTeams()

        val result1 = teams.getOrAwaitValue()
        val result2 = teams.getOrAwaitValue()

        assertThat(result1.status).isEqualTo(Status.LOADING)
        assertThat(result1.data).isNullOrEmpty()
        assertThat(result1.message).isNullOrEmpty()

        assertThat(result2.status).isEqualTo(Status.SUCCESS)
        assertThat(result2.data).isEqualTo(response)
    }
}