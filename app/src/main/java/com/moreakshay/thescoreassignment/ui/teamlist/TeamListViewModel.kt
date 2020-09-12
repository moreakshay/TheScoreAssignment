package com.moreakshay.thescoreassignment.ui.teamlist

import androidx.lifecycle.*
import com.moreakshay.thescoreassignment.data.TheScoreRepository
import com.moreakshay.thescoreassignment.ui.teamlist.domainmodels.Team
import com.moreakshay.thescoreassignment.utils.network.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TeamListViewModel @Inject constructor(
    private val repository: TheScoreRepository
) : ViewModel() {
    private val refreshLiveData = MutableLiveData(Any())

    enum class SortOrder {
        NONE,
        ASCENDING
    }

    private val sortOrder = MutableLiveData(SortOrder.NONE) //  should come from SavedStateHandle

    val teamList: LiveData<Resource<List<Team>>> = refreshLiveData.switchMap { _ ->
        sortOrder.switchMap { sortOrder ->
            liveData(context = Dispatchers.IO + viewModelScope.coroutineContext) {
                emitSource(
                    when (sortOrder) {
                        SortOrder.NONE -> repository.getAllTeams()
                        SortOrder.ASCENDING -> repository.getAllTeams() // filtered/sorted
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