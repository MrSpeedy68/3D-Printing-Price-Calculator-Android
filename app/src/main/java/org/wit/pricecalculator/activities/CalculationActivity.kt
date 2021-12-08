package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp

class CalculationActivity : AppCompatActivity(){

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}