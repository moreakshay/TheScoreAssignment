package com.moreakshay.thescoreassignment.teamlist

import androidx.lifecycle.*
import com.moreakshay.thescoreassignment.data.TheScoreRepository
import com.moreakshay.thescoreassignment.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.network.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TeamListViewModel @Inject constructor(private val repository: TheScoreRepository) : ViewModel() {

    val teamList: LiveData<Resource<List<Team>>> = liveData(context = Dispatchers.IO + viewModelScope.coroutineContext) {
        emitSource(repository.getAllTeams())
    }
}