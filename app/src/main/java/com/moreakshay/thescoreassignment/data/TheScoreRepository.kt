package com.moreakshay.thescoreassignment.data

import androidx.lifecycle.LiveData
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.local.entities.toPlayer
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.data.remote.dtos.createPlayerList
import com.moreakshay.thescoreassignment.data.remote.dtos.toTeamEntity
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import com.moreakshay.thescoreassignment.ui.teamlist.SortOrder
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.network.NetworkBoundResource
import com.moreakshay.thescoreassignment.utils.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ApplicationScope
class TheScoreRepository @Inject constructor(
    private val local: TheScoreDatabase,
    private val remote: ApiService
) {
    fun getAllTeams(sortOrder: SortOrder = SortOrder.NAME): LiveData<Resource<List<Team>>> {
        return object : NetworkBoundResource<List<Team>, List<NbaTeamListResponse>>() {
            override suspend fun saveCallResult(item: List<NbaTeamListResponse>) {
                local.runInTransaction {
                    item.forEach { teamListResponse ->
                        local.teamDao().insert(teamListResponse.toTeamEntity())
                        local.playerDao().insertAll(teamListResponse.createPlayerList())
                    }
                }
            }

            override fun shouldFetch(data: List<Team>?): Boolean =
                (data ?: emptyList()).isEmpty()

            override fun loadFromDb(): LiveData<List<Team>> {
                return when (sortOrder) {
                    SortOrder.NAME -> local.teamDao().getAllTeamsWithPlayers().toPlayer()
                    SortOrder.WINS -> local.teamDao().getAllTeamsWithPlayersSortByWins().toPlayer()
                    SortOrder.LOSSES -> local.teamDao().getAllTeamsWithPlayersSortedByLosses().toPlayer()
                    else -> local.teamDao().getAllTeamsWithPlayers().toPlayer()
                }
            }

            override suspend fun createCall(): List<NbaTeamListResponse> = remote.getNbaTeamList()

        }.asLiveData()
    }
}