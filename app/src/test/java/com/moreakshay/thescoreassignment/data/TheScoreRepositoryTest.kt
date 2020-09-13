package com.moreakshay.thescoreassignment.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.moreakshay.thescoreassignment.MainCoroutineScopeRule
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.local.daos.PlayerDao
import com.moreakshay.thescoreassignment.data.local.daos.TeamDao
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.data.local.entities.toDomainModel
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.data.remote.dtos.toEntity
import com.moreakshay.thescoreassignment.data.remote.dtos.toTeamEntity
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.TestUtils
import com.moreakshay.thescoreassignment.utils.argumentCaptor
import com.moreakshay.thescoreassignment.utils.mock
import com.moreakshay.thescoreassignment.utils.network.Resource
import getOrAwaitValue
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.apache.maven.model.Contributor
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as whenever

@RunWith(JUnit4::class)
class TheScoreRepositoryTest {

    private lateinit var repository: TheScoreRepository
    private val teamDao = Mockito.mock(TeamDao::class.java)
    private val playerDao = Mockito.mock(PlayerDao::class.java)
    private val service = mockk<ApiService>()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val mainCoroutineScopeRule = MainCoroutineScopeRule()


    @Before
    fun init() {
        val db = Mockito.mock(TheScoreDatabase::class.java)
        whenever(db.teamDao()).thenReturn(teamDao)
        whenever(db.playerDao()).thenReturn(playerDao)
        whenever(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = TheScoreRepository(db, service)

        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadTeamWithPlayers() = runBlockingTest {
        pauseDispatcher {
            val dbData = MutableLiveData<List<TeamWithPlayers>>()
            whenever(teamDao.getAllTeamsWithPlayers()).thenReturn(dbData)

            val data = repository.getAllTeams()
            verifyZeroInteractions(teamDao)

            coVerify(inverse = true) { service.getNbaTeamList() }

            val call = listOf(
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

            coEvery { service.getNbaTeamList() } returns call

            val observer = mock<Observer<Resource<List<Team>>>>()
            data.observeForever(observer)

            runCurrent()

            val updatedDbData = MutableLiveData<List<TeamWithPlayers>>()
            whenever(teamDao.getAllTeamsWithPlayers()).thenReturn(updatedDbData)
            dbData.value = emptyList()

            // coVerify { service.getNbaTeamList() }
            val inserted = argumentCaptor<List<TeamEntity>>()
            // empty list is a workaround for null capture return
            verify(teamDao).insertAll(inserted.capture() ?: emptyList())

            MatcherAssert.assertThat(inserted.value.size, CoreMatchers.`is`(1))

            updatedDbData.value = call.map { it.toEntity() }
            verify(observer).onChanged(Resource.success(call.map { it.toEntity().toDomainModel() }))
        }
    }
}