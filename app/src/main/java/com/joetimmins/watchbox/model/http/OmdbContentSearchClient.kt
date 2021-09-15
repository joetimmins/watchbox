package com.joetimmins.watchbox.model.http

import com.joetimmins.watchbox.model.ApiSearchResults
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbContentSearchClient {
    @GET("/")
    suspend fun search(
        @Query("s") searchTerm: String,
        @Query("type") contentType: String,
        @Query("apikey") apiKey: String = omdbApiKey
    ): ApiSearchResults
}