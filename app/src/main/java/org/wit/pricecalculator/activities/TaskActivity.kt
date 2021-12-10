package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.Location
import timber.log.Timber.i

class TaskActivity  : AppCompatActivity() {

    lateinit var app: MainApp
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val locationButton = findViewById<Button>(R.id.taskLocation)

        locationButton.setOnClickListener {
            i ("Set Location Pressed")
        }

        registerMapCallback()

        locationButton.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }


    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { i("Map Loaded") }
    }
}