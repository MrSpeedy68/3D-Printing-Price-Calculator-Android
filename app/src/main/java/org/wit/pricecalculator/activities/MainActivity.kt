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

        materialsButton.setOnClickListener {
            openMaterialsActivity()
        }

        printersButton.setOnClickListener {
            openPrintersActivity()
        }

        usersButton.setOnClickListener {
            openUserActivity()
        }


    }

    private fun openMaterialsActivity() {
        val launcherIntent = Intent(this, MaterialListActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openPrintersActivity() {
        val launcherIntent = Intent(this, PrinterActivity::class.java)
        startActivity(launcherIntent)
    }

    private fun openUserActivity() {
        val launcherIntent = Intent(this, UserActivity::class.java)
        startActivity(launcherIntent)
    }
}