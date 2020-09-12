package com.moreakshay.thescoreassignment.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.utils.MockResponseFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


@Suppress("SameParameterValue")
@RunWith(JUnit4::class)
class ApiServiceTest {

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

    @ExperimentalCoroutinesApi
    @Test
    fun getNbaTeamList_completeApiResponse_returnTrue() = runBlockingTest {
        val teams  = service.getNbaTeamList()
        enqueueResponse("SmallTeamResponse.json")
        var request: RecordedRequest
        withContext(Dispatchers.IO){
            request = server.takeRequest()
        }
        assertThat(request.path).isEqualTo("/nba-team-viewer/master/input.json")
        //team list
        assertThat(teams).isNotNull()
        assertThat(teams).isNotEmpty()
        //team
        val team = teams[0]
        val sampleTeam = getSampleTeam()
        assertThat(team.id).isEqualTo(sampleTeam.id)
        assertThat(team.fullName).isEqualTo(sampleTeam.fullName)
        assertThat(team.losses).isEqualTo(sampleTeam.losses)
        assertThat(team.wins).isEqualTo(sampleTeam.wins)
        assertThat(team.players).isNotNull()
        assertThat(team.players).isNotEmpty()

        //players
        val player = team.players?.get(0)
        val samplePlayer = sampleTeam.players?.get(0)
        assertThat(player).isNotNull()
        assertThat(player?.id).isEqualTo(samplePlayer?.id)
        assertThat(player?.firstName).isEqualTo(samplePlayer?.firstName)
        assertThat(player?.lastName).isEqualTo(samplePlayer?.lastName)
        assertThat(player?.position).isEqualTo(samplePlayer?.position)
        assertThat(player?.number).isEqualTo(samplePlayer?.number)
    }

    //TODO: check response with null id and null/failure response (will have to create new .json file)

    private fun enqueueResponse(fileName: String) {
        server.enqueue(MockResponse().setBody(MockResponseFileReader(fileName).content))
    }

    private fun getSampleTeam() = NbaTeamListResponse(
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

}