package com.fahruaz.storyapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoryModel(
    var name: String? = null,
    var photoUrl: String? = null,
    var description: String? = null,
    var lat: Double? = null,
    var lng: Double? = null
): Parcelable