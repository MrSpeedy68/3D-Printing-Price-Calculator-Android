package org.wit.pricecalculator.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityPrinterBinding
import org.wit.pricecalculator.helpers.showImagePicker
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.PrinterModel
import timber.log.Timber

class PrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrinterBinding
    var printer = PrinterModel()
    lateinit var app: MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("printer_edit")) {
            edit = true
            printer = intent.extras?.getParcelable("printer_edit")!!

            binding.printerName.setText(printer.name)
            binding.printerPrice.setText(printer.price.toString())
            binding.printerWattUsage.setText(printer.wattUsage.toString())
            binding.investReturn.setText(printer.investmentReturn.toString())

            binding.btnAdd.text = getString(R.string.button_savePrinter)
            binding.chooseImage.text = getString(R.string.button_changeImage)

            Picasso.get()
                .load(printer.image)
                .into(binding.printerImage)
        }

        binding.btnAdd.setOnClickListener() {
            printer.name = binding.printerName.text.toString()
            printer.price = binding.printerPrice.text.toString().toFloat()
            printer.wattUsage = binding.printerWattUsage.text.toString().toInt()
            printer.investmentReturn = binding.investReturn.text.toString().toInt()

            if (printer.name.isEmpty()) {
                Snackbar.make(it, R.string.no_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.printers.update(printer.copy())
                } else {
                    app.printers.create(printer.copy())
                }
            }
            Timber.i("add Button Pressed: $printer")
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            //i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_printer, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_delete -> { app.printers.delete(printer) ; finish() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            printer.image = result.data!!.data!!
                            Picasso.get()
                                .load(printer.image)
                                .into(binding.printerImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}