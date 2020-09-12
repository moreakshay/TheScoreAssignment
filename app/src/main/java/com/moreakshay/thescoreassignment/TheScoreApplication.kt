package com.moreakshay.thescoreassignment

import android.app.Application
import com.moreakshay.thescoreassignment.injection.components.AppComponent
import com.moreakshay.thescoreassignment.injection.components.DaggerAppComponent
import com.moreakshay.thescoreassignment.injection.modules.AppModule

class TheScoreApplication: Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        buildAppComponent()
    }

    private fun buildAppComponent(){
        component = DaggerAppComponent.factory().create(this)
        component.inject(this)
    }
}