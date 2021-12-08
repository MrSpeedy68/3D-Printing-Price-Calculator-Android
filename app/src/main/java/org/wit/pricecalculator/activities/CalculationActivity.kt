package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.MaterialsModel
import org.wit.pricecalculator.models.PrinterModel
import org.wit.pricecalculator.models.UserModel

class CalculationActivity : AppCompatActivity(){

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

    }

    //Calculates the total filament cost by getting cost per gram and multiplying by total grams used
    fun TotalFilamentCost(aMat: MaterialsModel, modelWeight: Int) : Float {
        var pricePerGram = aMat.price / aMat.weight

        var totalFilamentCost = pricePerGram * modelWeight

        totalFilamentCost = RoundToTwoDecimalPlaces(totalFilamentCost)

        println("The Price in material for this print is : $totalFilamentCost")

        return totalFilamentCost
    }

    //Calculates the electricity cost by converting watts into Kwh and multiplying by how many hours of printing was done
    fun ElectricityCost(aPrinter: PrinterModel, hours: Int, minutes: Int, aUser: UserModel) : Float {

        var printerkwh : Float = aPrinter.wattUsage.toFloat()
        var kwhUsed = printerkwh / 1000
        var totalElectricityCost = ((kwhUsed * GetTimeInHoursDecimal(hours, minutes)) * aUser.energyCost).toFloat()

        totalElectricityCost = RoundToTwoDecimalPlaces(totalElectricityCost)

        println("The Total Electricity Cost for this print is : ${aUser.currency} $totalElectricityCost")

        return totalElectricityCost
    }

    //Gets the investment return in months of the printer and turns it into hours then multiplying by hours of printing
    fun PrinterCosts(aPrinter: PrinterModel, hours: Int, minutes: Int) : Float {
        var monthsToHours = aPrinter.investmentReturn * 720 //Turn Months into days x30 and days into hours x24 which equals 720
        var printerCosts: Float = ((aPrinter.price / monthsToHours) * GetTimeInHoursDecimal(hours, minutes)).toFloat()

        printerCosts = RoundToTwoDecimalPlaces(printerCosts)

        println("The total Printer Depriciation Cost for this print is : $printerCosts")

        return  printerCosts
    }

    //Total cost combines all the calculations to get a total cost for printing
    fun TotalPrintCost(aMat: MaterialsModel, aPrinter: PrinterModel, aUser: UserModel, modelWeight: Int, hours: Int, minutes: Int) : Float {
        var totalPrintCost = TotalFilamentCost(aMat,modelWeight) + ElectricityCost(aPrinter, hours, minutes, aUser) + PrinterCosts(aPrinter, hours, minutes)

        totalPrintCost = RoundToTwoDecimalPlaces(totalPrintCost)

        println("The total cost for this print is : ${aUser.currency} $totalPrintCost")

        return totalPrintCost
    }

//Helper Functions

    //Turns hours and mins into a single decimal
    fun GetTimeInHoursDecimal(hours: Int, minutes: Int) : Float {
        var tempHours : Float = (minutes / 60).toFloat()

        return hours + tempHours
    }

    fun RoundToTwoDecimalPlaces(num: Float): Float {

        return String.format("%.2f", num).toFloat()
    }
}