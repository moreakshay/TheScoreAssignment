package com.moreakshay.thescoreassignment.ui.teamlist.domainmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val id: Int,
    val name: String,
    val wins: Int,
    val losses: Int,
    val players: List<Player>
) : Parcelable {
    @Parcelize
    data class Player(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val position: String,
        val number: Int
    ) : Parcelable
}
