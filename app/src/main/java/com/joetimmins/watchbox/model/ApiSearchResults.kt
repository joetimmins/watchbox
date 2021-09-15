package com.joetimmins.watchbox.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSearchResults(
    @Json(name = "Search") val results: List<ApiSearchResult>
)

@JsonClass(generateAdapter = true)
data class ApiSearchResult(
    @Json(name = "Title") val title: String,
    @Json(name = "Year") val yearRange: String,
    @Json(name = "imdbID") val imdbId: String,
    @Json(name = "Type") val contentType: String,
    @Json(name = "Poster") val posterUrl: String,
)