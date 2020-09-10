package com.moreakshay.thescoreassignment.injection.components

import android.content.Context
import com.moreakshay.thescoreassignment.TheScoreApplication
import com.moreakshay.thescoreassignment.data.TheScoreRepository
import dagger.Component
import com.moreakshay.thescoreassignment.injection.modules.AppModule
import com.moreakshay.thescoreassignment.injection.modules.RepositoryModule
//import moreakshay.com.mine.injection.modules.RepositoryModule
import com.moreakshay.thescoreassignment.injection.modules.ViewModelModule
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import com.moreakshay.thescoreassignment.teamlist.TeamListActivity

@ApplicationScope
@Component(modules = [AppModule::class, RepositoryModule::class, ViewModelModule::class])
interface AppComponent {

    @ApplicationContext
    fun getContext(): Context

    fun getRepository(): TheScoreRepository
    fun inject(app: TheScoreApplication)
    fun inject(activity: TeamListActivity)

}