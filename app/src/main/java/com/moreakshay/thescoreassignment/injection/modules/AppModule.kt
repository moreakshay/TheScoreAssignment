package com.moreakshay.thescoreassignment.injection.modules

import android.content.Context
import com.moreakshay.thescoreassignment.TheScoreApplication
import dagger.Module
import dagger.Provides
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope

@Module
class AppModule {
    @ApplicationContext
    @ApplicationScope
    @Provides
    fun getContext(app: TheScoreApplication): Context {
        return app
    }
}