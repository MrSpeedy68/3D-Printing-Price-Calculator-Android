package org.wit.pricecalculator.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskModel(var customerName: String = "",
                     var taskDescription: String = "",
                     var taskCost: Float = 0f,
                     var shippingCost: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
