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
        var edit = false
        binding = ActivityMaterialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("material_edit")) {
            edit = true
            material = intent.extras?.getParcelable("material_edit")!!
            binding.materialName.setText(material.name)
            binding.materialType.setText(material.type)
            binding.materialWeight.setText(material.weight.toString())
            binding.materialPrice.setText(material.price.toString())
            binding.btnAdd.text = getString(R.string.button_saveMaterial)
        }

        binding.btnAdd.setOnClickListener() {
            material.name = binding.materialName.text.toString()
            material.type = binding.materialType.text.toString()
            material.weight = binding.materialWeight.text.toString().toInt()
            material.price = binding.materialPrice.text.toString().toFloat()
            if (material.name.isEmpty()) {
                Snackbar.make(it,R.string.no_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.materials.update(material.copy())
                } else {
                    app.materials.create(material.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
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
