package org.wit.pricecalculator.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityMaterialBinding
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.MaterialsModel


class MaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterialBinding
    var material = MaterialsModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("material_edit")) {
            material = intent.extras?.getParcelable("material_edit")!!
            binding.materialName.setText(material.name)
            binding.materialType.setText(material.type)
            binding.materialWeight.setText(material.weight.toString())
            binding.materialPrice.setText(material.price.toString())

            binding.btnAdd.text = getString(R.string.button_saveMaterial)

            //app.placemarks.update(placemark)
            binding.btnAdd.setOnClickListener() {
                material.name = binding.materialName.text.toString()
                material.type = binding.materialType.text.toString()
                material.weight = binding.materialWeight.text.toString().toInt()
                material.price = binding.materialPrice.text.toString().toFloat()
                if (material.name.isNotEmpty()) {
                    app.materials.update(material)
                    //app.placemarks.create(placemark.copy())
                    setResult(RESULT_OK)
                    finish()
                }
                else {
                    Snackbar
                        .make(it,getString(R.string.no_title), Snackbar.LENGTH_LONG) // Cant access string for some reason ? why bother Resources.getSystem().getString(R.string.no_title)
                        .show()
                }
            }

        }
        else {
            binding.btnAdd.setOnClickListener() {
                material.name = binding.materialName.text.toString()
                material.type = binding.materialType.text.toString()
                material.weight = binding.materialWeight.text.toString().toInt()
                material.price = binding.materialPrice.text.toString().toFloat()
                if (material.name.isNotEmpty()) {
                    app.materials.create(material.copy())
                    setResult(RESULT_OK)
                    finish()
                }
                else {
                    Snackbar
                        .make(it,getString(R.string.no_title), Snackbar.LENGTH_LONG) // Cant access string for some reason ? why bother Resources.getSystem().getString(R.string.no_title)
                        .show()
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_material, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}
