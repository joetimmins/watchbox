package com.joetimmins.watchbox.model.http

interface OmdbHttpClient : OmdbContentSearchClient, OmdbContentDetailClient

const val omdbApiKey = "261d3e09"