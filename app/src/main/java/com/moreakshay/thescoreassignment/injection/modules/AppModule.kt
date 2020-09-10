package com.moreakshay.thescoreassignment.injection.modules

import android.content.Context
import com.moreakshay.thescoreassignment.TheScoreApplication
import dagger.Module
import dagger.Provides
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope

@Module
class AppModule(var app: TheScoreApplication) {

    @ApplicationContext
    @ApplicationScope
    @Provides
    fun getContext(): Context{
        return app
    }
}