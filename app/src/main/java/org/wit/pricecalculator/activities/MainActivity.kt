package org.wit.pricecalculator.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.wit.pricecalculator.R
import org.wit.pricecalculator.main.MainApp

class MainActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val materialsButton = findViewById<Button>(R.id.btnMaterials)
        val printersButton = findViewById<Button>(R.id.btnPrinters)
        val usersButton = findViewById<Button>(R.id.btnUsers)
        val calcButton = findViewById<Button>(R.id.btnCalc)
        val taskButton = findViewById<Button>(R.id.btnTask)

        materialsButton.setOnClickListener {
            openMaterialsActivity()
        }

        printersButton.setOnClickListener {
            openPrintersActivity()
        }

        usersButton.setOnClickListener {
            openUserActivity()
        }

        calcButton.setOnClickListener {
            openCalcActivity()
        }

        taskButton.setOnClickListener {
            openTaskActivity()
        }


    }

    private fun openMaterialsActivity() {
        val launcherIntent = Intent(this, MaterialListActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openPrintersActivity() {
        val launcherIntent = Intent(this, PrinterListActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openUserActivity() {
        val launcherIntent = Intent(this, UserListActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openCalcActivity() {
        val launcherIntent = Intent(this, CalculationActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openTaskActivity() {
        val launcherIntent = Intent(this, TaskActivity::class.java)
        startActivity(launcherIntent)
    }
}