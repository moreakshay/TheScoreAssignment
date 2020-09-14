package com.moreakshay.thescoreassignment.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.google.common.truth.Truth.assertThat
import com.moreakshay.thescoreassignment.MainCoroutineScopeRule
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.local.daos.PlayerDao
import com.moreakshay.thescoreassignment.data.local.daos.TeamDao
import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamEntity
import com.moreakshay.thescoreassignment.data.local.entities.TeamWithPlayers
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.TestUtils
import com.moreakshay.thescoreassignment.utils.network.Resource
import getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)
class TheScoreRepositoryTest {

    private lateinit var repository: TheScoreRepository

    @RelaxedMockK
    private val teamDao = mockk<TeamDao>(relaxed = true)

    @RelaxedMockK
    private val playerDao = mockk<PlayerDao>(relaxed = true)

    @RelaxedMockK
    private val service = mockk<ApiService>(relaxed = true)

    @RelaxedMockK
    private val db = mockk<TheScoreDatabase>(relaxed = true)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val coroutineScopeRule = MainCoroutineScopeRule()


    @Before
    fun init() {
        every { db.teamDao() } returns teamDao
        every { db.playerDao() } returns playerDao
        every { db.runInTransaction(allAny()) } answers { callOriginal() }
        repository = TheScoreRepository(db, service)

        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testTeamsFromNetworkAreSavedToDatabaseIfDatabaseIsEmpty() = coroutineScopeRule.runBlockingTest {
        pauseDispatcher()

        // GIVEN
        val dbData = liveData<List<TeamWithPlayers>> {
            delay(1000)
            emit(emptyList())
        }

        every { teamDao.getAllTeamsWithPlayers() } returns dbData

        val apiResponse = TestUtils.getApiResponse()
        coEvery { service.getNbaTeamList() } coAnswers {
            delay(3000L)
            apiResponse
        }

        // WHEN
        val domainData = repository.getAllTeams()

        val observer = Observer<Resource<List<Team>>> {}
        domainData.observeForever(observer)

        // THEN
        advanceTimeBy(500L)

        try {
            domainData.getOrAwaitValue()
            Assert.fail("Should have no value")
        } catch (e: TimeoutException) {
            // OK
        }

        advanceTimeBy(1000L)

        val firstResource = domainData.getOrAwaitValue()
        assertThat(firstResource).isEqualTo(Resource.loading(emptyList<Team>()))

        advanceUntilIdle()

        resumeDispatcher()

        val lastResource = domainData.getOrAwaitValue()

        assertThat(lastResource).isEqualTo(Resource.success(emptyList<Team>())) // it's empty list because the mock is not stateful

        verify { teamDao.getAllTeamsWithPlayers() }

        coVerify { service.getNbaTeamList() }

        verify { db.runInTransaction(allAny()) }

        val teamSlot = slot<TeamEntity>()
        val playerSlot = slot<List<PlayerEntity>>()

        verify { teamDao.insert(capture(teamSlot)) }
        verify { playerDao.insertAll(capture(playerSlot)) }

        val apiTeam = apiResponse.sortedBy { it.fullName }[0]
        val teamEntity = teamSlot.captured

        assertThat(apiTeam.id).isEqualTo(teamEntity.id)
        assertThat(apiTeam.fullName).isEqualTo(teamEntity.name)

        assertThat(apiTeam.losses).isEqualTo(teamEntity.losses)
        assertThat(apiTeam.wins).isEqualTo(teamEntity.wins)

        val apiFirstPlayer = checkNotNull(apiTeam.players?.get(0))
        val playerEntity = playerSlot.captured[0]

        assertThat(apiFirstPlayer.id).isEqualTo(playerEntity.id)
        assertThat(apiFirstPlayer.lastName).isEqualTo(playerEntity.lastName)
        assertThat(apiFirstPlayer.firstName).isEqualTo(playerEntity.firstName)
        assertThat(apiFirstPlayer.number).isEqualTo(playerEntity.number)
    }
}