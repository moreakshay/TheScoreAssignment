package com.moreakshay.thescoreassignment.injection.modules

import android.content.Context
import com.moreakshay.thescoreassignment.data.remote.ApiService
import dagger.Module
import dagger.Provides
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import com.moreakshay.thescoreassignment.utils.constants.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class RepositoryModule {

    @ApplicationScope
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @ApplicationScope
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    @ApplicationScope
    @Provides
    fun proviesHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @ApplicationScope
    @Provides
    fun proviedHeaderInterceptor(): Interceptor {
        return Interceptor {
            val url = it.request()
                    .url()
                    .newBuilder()
//                    .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build()
            val request = it.request()
                    .newBuilder()
                    .url(url)
                    .build()
            return@Interceptor it.proceed(request)
        }
    }

    /*@ApplicationScope
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MineDatabase {
        return Room.databaseBuilder(context, MineDatabase::class.java, DATABASE_NAME).build()
    }*/
}
