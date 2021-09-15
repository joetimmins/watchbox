package com.joetimmins.watchbox.model.http

import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbContentDetailClient {
    @GET
    suspend fun detail(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = omdbApiKey
    )
}