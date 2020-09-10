package com.moreakshay.thescoreassignment.injection.components

import android.content.Context
import dagger.Component
import com.moreakshay.thescoreassignment.injection.modules.AppModule
//import moreakshay.com.mine.injection.modules.RepositoryModule
import com.moreakshay.thescoreassignment.injection.modules.ViewModelModule
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope

@ApplicationScope
@Component(modules = [AppModule::class, /*RepositoryModule::class,*/ ViewModelModule::class])
interface AppComponent {

    @ApplicationContext
    fun getContext(): Context

//    fun getRepository(): MineRepository
//    fun inject(app: MineApplication)

}