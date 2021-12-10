package org.wit.pricecalculator.activities

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp
import timber.log.Timber.i

class TaskActivity  : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val locationButton = findViewById<Button>(R.id.taskLocation)

        locationButton.setOnClickListener {
            i ("Set Location Pressed")
        }
    }
}