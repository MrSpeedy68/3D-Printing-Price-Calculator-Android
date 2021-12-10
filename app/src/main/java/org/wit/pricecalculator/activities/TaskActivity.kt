package org.wit.pricecalculator.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.GeoPoint
import org.wit.pricecalculator.models.Location
import timber.log.Timber.i
import java.lang.NumberFormatException

class TaskActivity  : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var location = Location(52.245696, -7.139102, 15f)
    var address = "153 Hennessys Road, Waterford"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val locationButton = findViewById<Button>(R.id.taskLocation)
        val testButton = findViewById<Button>(R.id.testLocation)

        locationButton.setOnClickListener {
            i ("Set Location Pressed")
        }


        testButton.setOnClickListener {
            i ("NEW LOCATION ++++++++++++++++++++++++++++++++++++++++++++")
            i (getLocationFromAddress(address).toString())
        }



        registerMapCallback()

        locationButton.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(this)
        val address: List<Address>
        var p1: GeoPoint? = null
        return try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            location.getLatitude()
            location.getLongitude()
            p1 = GeoPoint(
                (location.getLatitude()) as Double,
                (location.getLongitude()) as Double
            )
            p1
        }
        catch (e: NumberFormatException) {null}
    }

}