package com.moreakshay.thescoreassignment.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object DispatcherProvider {
    var IO: CoroutineDispatcher = Dispatchers.IO
    var Default: CoroutineDispatcher = Dispatchers.Default
    var Main: CoroutineDispatcher = Dispatchers.Main
    var Unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}