package com.joetimmins.watchbox.model

val twinPeaksContentSearchResult = ContentSearchResult(
    title = "Twin Peaks",
    contentType = ContentType.Series,
    airDate = AirDate.Ongoing(1990, 1991),
    imdbId = "tt0098936",
    posterUrl = "https://m.media-amazon.com/images/M/MV5BMTExNzk2NjcxNTNeQTJeQWpwZ15BbWU4MDcxOTczOTIx._V1_SX300.jpg",
)

val twinPeaks1992ContentSearchResult = twinPeaksContentSearchResult.copy(
    title = "Twin Peaks 1992",
    airDate = AirDate.Single(1992),
    imdbId = "tt0098937",
)