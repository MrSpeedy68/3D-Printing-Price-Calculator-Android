package org.wit.pricecalculator.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.squareup.picasso.Picasso
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityMaterialBinding
import org.wit.pricecalculator.helpers.showImagePicker
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.MaterialsModel
import timber.log.Timber.i
import com.google.firebase.ktx.Firebase


class MaterialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterialBinding
    private lateinit var database: DatabaseReference
    var material = MaterialsModel()
    lateinit var app: MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

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


            Picasso.get()
                .load(material.image)
                .into(binding.materialImage)
            if(material.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.button_changeImage)
            }
        }

        binding.btnAdd.setOnClickListener() {
            material.name = binding.materialName.text.toString()
            material.type = binding.materialType.text.toString()
            material.weight = binding.materialWeight.text.toString().toInt()
            material.price = binding.materialPrice.text.toString().toFloat()

//            database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
//            database.child(material.name).setValue(material).addOnSuccessListener {



            database = Firebase.database("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
            database.child(material.name).setValue(material).addOnSuccessListener {


                Toast.makeText(this, "Successfully Saved Material", Toast.LENGTH_SHORT).show()

                setResult(RESULT_OK)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to Save Material", Toast.LENGTH_SHORT).show()
            }

//            if (material.name.isEmpty()) {
//                Snackbar.make(it,R.string.no_title, Snackbar.LENGTH_LONG)
//                    .show()
//            } else {
//                if (edit) {
//                    app.materials.update(material.copy())
//                } else {
//
//                    app.materials.create(material.copy())
//                }
//            }
//            i("add Button Pressed: $material")
//            setResult(RESULT_OK)
//            finish()
        }

        binding.chooseImage.setOnClickListener {
            //i("Select image")
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_material, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_delete -> { app.materials.delete(material) ; finish() }
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
                            i("Got Result ${result.data!!.data}")
                            material.image = result.data!!.data!!
                            Picasso.get()
                                .load(material.image)
                                .into(binding.materialImage)
                            binding.chooseImage.setText(R.string.button_changeImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
