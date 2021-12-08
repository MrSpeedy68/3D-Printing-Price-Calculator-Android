package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.pricecalculator.R
import org.wit.pricecalculator.adapters.PrinterAdapter
import org.wit.pricecalculator.adapters.PrinterListiner
import org.wit.pricecalculator.databinding.ActivityPrinterListBinding
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.PrinterModel

class PrinterListActivity : AppCompatActivity(), PrinterListiner {

    lateinit var app: MainApp
    private lateinit var binding: ActivityPrinterListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrinterListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PrinterAdapter(app.printers.findAll(),this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, PrinterActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrinterClick(printers: PrinterModel) { //Clicking on printer takes us to printers edit
        val launcherIntent = Intent(this, PrinterActivity::class.java)
        launcherIntent.putExtra("printer_edit", printers)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

}