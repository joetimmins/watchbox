package com.joetimmins.watchbox.model

enum class ContentType(val rawContentType: String) {
    Movie("movie"),
    Series("series");

    companion object {
        fun fromRawValue(rawValue: String): ContentType = values().first {
            it.rawContentType == rawValue.lowercase()
        }
    }
}