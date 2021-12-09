package org.wit.pricecalculator.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityCalculationBinding
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.MaterialsModel
import org.wit.pricecalculator.models.PrinterModel
import org.wit.pricecalculator.models.UserModel
import timber.log.Timber.i

class CalculationActivity : AppCompatActivity(){
    private lateinit var binding: ActivityCalculationBinding
    lateinit var app: MainApp

    private lateinit var matSpinner: Spinner
    private lateinit var materialAdapter: ArrayAdapter<MaterialsModel>

    private lateinit var printSpinner: Spinner
    private lateinit var printerAdapter: ArrayAdapter<PrinterModel>

    private lateinit var usrSpinner: Spinner
    private lateinit var userAdapter: ArrayAdapter<UserModel>

    var selectedMaterial = MaterialsModel()
    var selectedPrinter = PrinterModel()
    var selectedUser = UserModel()


    @SuppressLint("TimberArgCount")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)
        binding = ActivityCalculationBinding.inflate(layoutInflater)
        setContentView((binding.root))
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        matSpinner = findViewById(R.id.materialSpinner)
        printSpinner = findViewById(R.id.printerSpinner)
        usrSpinner = findViewById(R.id.userSpinner)


        val calcButton = findViewById<Button>(R.id.btnCalculate)








        //Populate Material Spinner with all material objects
        val materialObjects = app.materials.findAll()

        materialAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, materialObjects)

        matSpinner.adapter = materialAdapter

        matSpinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMaterial = materialObjects[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Populate Printer Spinner with all printer objects
        val printerObjects = app.printers.findAll()

        printerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, printerObjects)

        printSpinner.adapter = printerAdapter

        printSpinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedPrinter = printerObjects[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Populate Material Spinner with all material objects
        val userObjects = app.users.findAll()

        userAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userObjects)

        usrSpinner.adapter = userAdapter

        usrSpinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedUser = userObjects[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        calcButton.setOnClickListener {
            //TotalPrintCost()
            i("model weight val: %s", binding.modelWeight.text)

            TotalPrintCost(selectedMaterial,selectedPrinter,selectedUser,binding.modelWeight.text.toString().toInt(),binding.timeHours.text.toString().toInt(),binding.timeMinutes.text.toString().toInt())
        }
    }




    //Calculates the total filament cost by getting cost per gram and multiplying by total grams used
    @SuppressLint("SetTextI18n")
    fun TotalFilamentCost(aMat: MaterialsModel, modelWeight: Int) : Float {
        var pricePerGram = aMat.price / aMat.weight

        var totalFilamentCost = pricePerGram * modelWeight

        totalFilamentCost = RoundToTwoDecimalPlaces(totalFilamentCost)

        println("The Price in material for this print is : $totalFilamentCost")
        binding.MatCostText.text = "Total Material Cost: $totalFilamentCost"

        return totalFilamentCost
    }

    //Calculates the electricity cost by converting watts into Kwh and multiplying by how many hours of printing was done
    @SuppressLint("SetTextI18n")
    fun ElectricityCost(aPrinter: PrinterModel, hours: Int, minutes: Int, aUser: UserModel) : Float {

        var printerkwh : Float = aPrinter.wattUsage.toFloat()
        var kwhUsed = printerkwh / 1000
        var totalElectricityCost = ((kwhUsed * GetTimeInHoursDecimal(hours, minutes)) * aUser.energyCost).toFloat()

        totalElectricityCost = RoundToTwoDecimalPlaces(totalElectricityCost)

        println("The Total Electricity Cost for this print is : ${aUser.currency} $totalElectricityCost")
        binding.EnergyCostText.text = "Total Energy Cost: $totalElectricityCost"

        return totalElectricityCost
    }

    //Gets the investment return in months of the printer and turns it into hours then multiplying by hours of printing
    @SuppressLint("SetTextI18n")
    fun PrinterCosts(aPrinter: PrinterModel, hours: Int, minutes: Int) : Float {
        var monthsToHours = aPrinter.investmentReturn * 720 //Turn Months into days x30 and days into hours x24 which equals 720
        var printerCosts: Float = ((aPrinter.price / monthsToHours) * GetTimeInHoursDecimal(hours, minutes)).toFloat()

        printerCosts = RoundToTwoDecimalPlaces(printerCosts)

        println("The total Printer Depriciation Cost for this print is : $printerCosts")
        binding.PrinterCostText.text = "Total Printer Cost: $printerCosts"

        return  printerCosts
    }

    //Total cost combines all the calculations to get a total cost for printing
    @SuppressLint("SetTextI18n")
    fun TotalPrintCost(aMat: MaterialsModel, aPrinter: PrinterModel, aUser: UserModel, modelWeight: Int, hours: Int, minutes: Int) : Float {
        var totalPrintCost = TotalFilamentCost(aMat,modelWeight) + ElectricityCost(aPrinter, hours, minutes, aUser) + PrinterCosts(aPrinter, hours, minutes)

        totalPrintCost = RoundToTwoDecimalPlaces(totalPrintCost)

        println("The total cost for this print is : ${aUser.currency} $totalPrintCost")

        binding.TotalCostText.text = "Total Printing Cost: $totalPrintCost"
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