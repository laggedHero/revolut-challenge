package net.laggedhero.revolut.challenge.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import net.laggedhero.revolut.challenge.feature.rates.data.CurrencyApi
import net.laggedhero.revolut.challenge.feature.rates.data.CurrencyRepositoryImpl
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hiring.revolut.codes/api/android/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun providesCurrencyApi(retrofit: Retrofit): CurrencyApi {
        return retrofit.create(CurrencyApi::class.java)
    }

    @Provides
    fun providesCurrencyRepository(currencyApi: CurrencyApi): CurrencyRepository {
        return CurrencyRepositoryImpl(currencyApi)
    }
}