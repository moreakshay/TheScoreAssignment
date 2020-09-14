package com.moreakshay.thescoreassignment.ui.teamlist

import androidx.lifecycle.*
import com.moreakshay.thescoreassignment.core.dispatchers.DispatcherProvider
import com.moreakshay.thescoreassignment.data.TheScoreRepository
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.network.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TeamListViewModel @Inject constructor(
    private val repository: TheScoreRepository
) : ViewModel() {
    private val refreshLiveData = MutableLiveData(Any())

    private val sortOrder = MutableLiveData(SortOrder.NAME) //  should come from SavedStateHandle

    val teamList: LiveData<Resource<List<Team>>> = refreshLiveData.switchMap { _ ->
        sortOrder.switchMap { sortOrder ->
            liveData(context = DispatcherProvider.IO + viewModelScope.coroutineContext) {
                emitSource(
                    when (sortOrder) {
                        SortOrder.NAME -> repository.getAllTeams(SortOrder.NAME)
                        SortOrder.WINS -> repository.getAllTeams(SortOrder.WINS)
                        SortOrder.LOSSES -> repository.getAllTeams(SortOrder.LOSSES)
                        else ->  repository.getAllTeams()
                    }
                )
            }
        }
    }

    fun refresh() {
        refreshLiveData.value = Any()
    }

    fun sortBy(order: SortOrder) {
        sortOrder.value = order
    }
}