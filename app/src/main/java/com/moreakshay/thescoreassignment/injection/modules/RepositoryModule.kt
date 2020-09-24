package com.moreakshay.thescoreassignment.injection.modules

import android.content.Context
import androidx.room.Room
import com.moreakshay.thescoreassignment.data.local.TheScoreDatabase
import com.moreakshay.thescoreassignment.data.remote.ApiService
import com.moreakshay.thescoreassignment.injection.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides
import com.moreakshay.thescoreassignment.injection.scopes.ApplicationScope
import com.moreakshay.thescoreassignment.utils.constants.BASE_URL
import com.moreakshay.thescoreassignment.utils.constants.DATABASE_NAME
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
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @ApplicationScope
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val url = chain.request()
                    .url()
                    .newBuilder()
//                    .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                    .build()
            val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
            return@Interceptor chain.proceed(request)
        }
    }

    @ApplicationScope
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TheScoreDatabase {
        return Room.databaseBuilder(context, TheScoreDatabase::class.java, DATABASE_NAME).build()
    }
}
