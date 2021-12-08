package org.wit.pricecalculator.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var userId: Long = 0,
                     var userName: String = "",
                     var labourCost: Float = 0.0f,
                     var energyCost: Float = 0.0f,
                     var currency: String = "$",
                     var image: Uri = Uri.EMPTY) : Parcelable
