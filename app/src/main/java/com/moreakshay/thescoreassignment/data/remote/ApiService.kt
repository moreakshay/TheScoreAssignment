package com.moreakshay.thescoreassignment.data.remote

import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import retrofit2.http.GET

interface ApiService {

    @GET("nba-team-viewer/master/input.json")
    suspend fun getNbaTeamList(): List<NbaTeamListResponse>
}