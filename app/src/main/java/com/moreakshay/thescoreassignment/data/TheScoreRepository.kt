package com.moreakshay.thescoreassignment.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.local.entities.PlayerEntity
import com.moreakshay.thescoreassignment.data.local.entities.toDomainModel
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.data.remote.dtos.NbaTeamListResponse
import com.moreakshay.thescoreassignment.data.remote.dtos.toEntity
import com.moreakshay.thescoreassignment.data.remote.dtos.toTeamEntity
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import com.moreakshay.thescoreassignment.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.network.NetworkBoundResource
import com.moreakshay.thescoreassignment.utils.network.Resource
import javax.inject.Inject

@ApplicationScope
class TheScoreRepository @Inject constructor(
    private val local: TheScoreDatabase,
    private val remote: ApiService
) {
    suspend fun getAllTeams(): LiveData<Resource<List<Team>>> {
        return object : NetworkBoundResource<List<Team>, List<NbaTeamListResponse>>() {
            override suspend fun saveCallResult(item: List<NbaTeamListResponse>) {
                item.forEach {
                    local.teamDao().insert(it.toTeamEntity())
                    local.playerDao().insertAll(createPlayerList(it))
                }
            }

            override fun shouldFetch(data: List<Team>?): Boolean {
                return true
            }

            override suspend fun loadFromDb(): LiveData<List<Team>> {
                return local.teamDao().getAllTeamsWithPlayers().toDomainModel()
            }

            override suspend fun createCall(): List<NbaTeamListResponse> {
                return remote.getNbaTeamList()
            }
        }.asLiveData()
    }

    private fun createPlayerList(team: NbaTeamListResponse): List<PlayerEntity> {
        val players = mutableListOf<PlayerEntity>()
        team.players?.forEach {
            if (it != null) {
                players.add(it.toEntity(team.id))
            }
        }
        return players
    }
}