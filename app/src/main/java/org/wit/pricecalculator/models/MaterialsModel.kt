package org.wit.pricecalculator.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MaterialsModel(var id: Long = 0,
                          var name: String = "",
                          var type: String = "",
                          var weight: Int = 0,
                          var price: Float = 0.0f,
                          var image: Uri = Uri.EMPTY) : Parcelable