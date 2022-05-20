package com.fahruaz.storyapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var token: String? = null
): Parcelable