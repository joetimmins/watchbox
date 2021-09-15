package com.joetimmins.watchbox.model.http

import androidx.annotation.VisibleForTesting
import com.joetimmins.watchbox.BuildConfig
import com.joetimmins.watchbox.model.http.OmdbHttpClient
import com.squareup.moshi.Moshi
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object OmdbService {
    val client: OmdbHttpClient by lazy {
        retrofit.create(OmdbHttpClient::class.java)
    }

    private val retrofit: Retrofit by lazy {
        retrofitBuilder(OMDB_BASE_URL.toHttpUrl())
            .build()
    }

    private val moshi: Moshi = Moshi.Builder()
        .build()

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptorForDebugBuild()
            .build()
    }

    @VisibleForTesting
    fun retrofitBuilder(baseUrl: HttpUrl): Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .baseUrl(baseUrl)
}

private fun OkHttpClient.Builder.addInterceptorForDebugBuild() = when (BuildConfig.DEBUG) {
    true -> addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    false -> this
}

private const val OMDB_BASE_URL = "https://www.omdbapi.com/"