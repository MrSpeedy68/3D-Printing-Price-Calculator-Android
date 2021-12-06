package org.wit.pricecalculator.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PrinterModel(var id: Long = 0,
                        var name: String = "",
                        var price: Float = 0.0f,
                        var wattUsage: Int = 0,
                        var investmentReturn: Int = 0,
                        var image: Uri = Uri.EMPTY ) : Parcelable
