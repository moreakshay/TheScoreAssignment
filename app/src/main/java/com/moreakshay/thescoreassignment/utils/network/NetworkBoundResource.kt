package com.moreakshay.thescoreassignment.utils.network

import androidx.lifecycle.*
import com.moreakshay.thescoreassignment.core.dispatchers.DispatcherProvider
import kotlinx.coroutines.Dispatchers

abstract class NetworkBoundResource<ResultType : Any, RequestType : Any> {
    private val responseHandler = ResponseHandler()

    fun asLiveData(): LiveData<Resource<ResultType>> =
        liveData(DispatcherProvider.IO) {
            val initialData = loadFromDb()

            val disposableHandle = emitSource(initialData.map { responseHandler.handleLoading(it) })

            try {
                if (shouldFetch(initialData.value)) {
                    val apiResponse = createCall()
                    saveCallResult(apiResponse)
                }
                disposableHandle.dispose()

                emitSource(loadFromDb().map { responseHandler.handleSuccess(it) })
            } catch (e: Exception) {
                disposableHandle.dispose()
                emitSource(loadFromDb().map { responseHandler.handleException(e, it) })
            }
        }

    protected abstract suspend fun saveCallResult(item: RequestType)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): LiveData<ResultType>

    protected abstract suspend fun createCall(): RequestType
}