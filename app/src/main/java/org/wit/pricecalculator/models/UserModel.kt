package org.wit.pricecalculator.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(var userName: String = "",
                     var labourCost: Double = 0.0,
                     var energyCost: Double = 0.0,
                     var currency: String = "$") : Parcelable
