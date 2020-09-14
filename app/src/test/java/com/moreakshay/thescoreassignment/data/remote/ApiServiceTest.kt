package com.moreakshay.thescoreassignment.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.moreakshay.thescoreassignment.MainCoroutineScopeRule
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.utils.MockResponseFileReader
import com.moreakshay.thescoreassignment.utils.TestUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("SameParameterValue")
@RunWith(JUnit4::class)
class ApiServiceTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val coroutineScopeRule = MainCoroutineScopeRule()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ApiService

    private lateinit var server: MockWebServer

    @Before
    fun createService() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() = server.shutdown()

    private fun enqueueResponse(fileName: String) {
        server.enqueue(MockResponse().setBody(MockResponseFileReader(fileName).content))
    }

    @Test
    fun getNbaTeamList_completeApiResponse_returnTrue() {
        enqueueResponse("SmallTeamResponse.json")
        // Once https://github.com/Kotlin/kotlinx.coroutines/issues/1204 is fixed this can be runBlockingTest
        val teams = runBlocking {
            service.getNbaTeamList()
        }
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/nba-team-viewer/master/input.json")
        //team list
        assertThat(teams).isNotNull()
        assertThat(teams).isNotEmpty()
        //team
        val team = teams[0]
        val compareTeam = firstTeam

        assertThat(team.id).isEqualTo(compareTeam.id)
        assertThat(team.fullName).isEqualTo(compareTeam.fullName)
        assertThat(team.losses).isEqualTo(compareTeam.losses)
        assertThat(team.wins).isEqualTo(compareTeam.wins)
        assertThat(team.players).isNotNull()
        assertThat(team.players).isNotEmpty()

        //players
        val player = checkNotNull(team.players?.get(0))
        val comparePlayer = checkNotNull(compareTeam.players?.get(0))

        assertThat(player.id).isEqualTo(comparePlayer.id)
        assertThat(player.firstName).isEqualTo(comparePlayer.firstName)
        assertThat(player.lastName).isEqualTo(comparePlayer.lastName)
        assertThat(player.position).isEqualTo(comparePlayer.position)
        assertThat(player.number).isEqualTo(comparePlayer.number)
    }

    @Test
    fun getNbaTeamList_emptyApiResponse_returnFalse(){
        server.enqueue(MockResponse().setResponseCode(200).setBody("[]"))
        val teams: List<NbaTeamListResponse>? = runBlocking {
            service.getNbaTeamList()
        }
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/nba-team-viewer/master/input.json")
        assertThat(teams).isEmpty()
    }


    private val firstTeam get() = TestUtils.getApiResponse()[0]
}