package com.moreakshay.thescoreassignment.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moreakshay.thescoreassignment.injection.keys.ViewModelKey
import com.moreakshay.thescoreassignment.ui.teamlist.TeamListViewModel
import com.moreakshay.thescoreassignment.utils.viewmodels.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TeamListViewModel::class)
    abstract fun bindMoviesViewModel(moviesViewModel: TeamListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}