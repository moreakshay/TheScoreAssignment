package com.moreakshay.thescoreassignment.injection.modules

import android.content.Context
import com.moreakshay.thescoreassignment.TheScoreApplication
import dagger.Module
import dagger.Provides
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import dagger.Binds

@Module
abstract class AppModule {
    @ApplicationContext
    @ApplicationScope
    @Binds
    abstract fun bindAppContext(app: TheScoreApplication): Context
}