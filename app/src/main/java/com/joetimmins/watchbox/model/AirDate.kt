package com.joetimmins.watchbox.model

sealed interface AirDate {
    data class Single(val yearReleased: Int) : AirDate
    data class Ongoing(val yearFirstShown: Int, val yearLastShown: Int?) : AirDate

    companion object {
        fun fromYearRange(yearRange: String): AirDate {
            val s = yearRange.split('–') // believe it or not ... this is *not* a regular dash -
            return when (s.size) {
                1 -> Single(s.first().toInt())
                2 -> Ongoing(s.first().toInt(), s.last().toIntOrNull())
                else -> throw IllegalArgumentException("Invalid year range.")
            }
        }
    }
}
