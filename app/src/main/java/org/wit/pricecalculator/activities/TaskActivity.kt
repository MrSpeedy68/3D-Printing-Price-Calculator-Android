package org.wit.pricecalculator.activities

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityTaskBinding
import org.wit.pricecalculator.helpers.showImagePicker
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.GeoPointModel
import org.wit.pricecalculator.models.Location
import org.wit.pricecalculator.models.TaskModel
import timber.log.Timber.i
import java.lang.NumberFormatException

class TaskActivity  : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    var task = TaskModel()
    lateinit var app: MainApp
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        var edit = false
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("task_edit")) {
            edit = true
            task = intent.extras?.getParcelable("task_edit")!!
            binding.customerName.setText(task.customerName)
            binding.taskDescription.setText(task.taskDescription)
            binding.taskAddress.setText(task.address)
            binding.taskCosts.setText(task.taskCost.toString())
            binding.shippingCosts.setText(task.shippingCost.toString())
            binding.btnAdd.text = getString(R.string.button_saveTask)

        }

        binding.btnAdd.setOnClickListener() {
            task.customerName = binding.customerName.text.toString()
            task.taskDescription = binding.taskDescription.text.toString()
            task.address = binding.taskAddress.text.toString()
            task.taskCost = binding.taskCosts.text.toString().toFloat()
            task.shippingCost = binding.shippingCosts.text.toString().toFloat()
            if(binding.taskAddress.text != null) {
                val coords: GeoPointModel? =
                    getLocationFromAddress(binding.taskAddress.text.toString())

                if (coords != null) {
                    task.lat = coords.latitude
                    task.lng = coords.longitude
                    task.zoom = 15f
                }
            }

            if (task.customerName.isEmpty()) {
                Snackbar.make(it,R.string.no_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.tasks.update(task.copy())
                } else {
                    app.tasks.create(task.copy())
                }
            }
            i("add Button Pressed: $task")
            setResult(RESULT_OK)
            finish()
        }

        registerMapCallback()

        binding.btnMap.setOnClickListener {

            //registerMapCallback()
            if(binding.taskAddress.text != null) {
                val coords: GeoPointModel? =
                    getLocationFromAddress(binding.taskAddress.text.toString())

                i("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
                i(coords.toString())

                if (coords != null) {
                    location = Location(coords.latitude, coords.longitude, 15f)

                    val launcherIntent = Intent(this, MapActivity::class.java)
                        .putExtra("location", location)
                    mapIntentLauncher.launch(launcherIntent)
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
            R.id.item_delete -> { app.tasks.delete(task) ; finish() }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    fun getLocationFromAddress(strAddress: String?): GeoPointModel? {
        val coder = Geocoder(this)
        val address: List<Address>
        var p1: GeoPointModel? = null
        return try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val loc: Address = address[0]
            loc.latitude
            loc.longitude
            p1 = GeoPointModel(
                (loc.latitude) as Double,
                (loc.longitude) as Double
            )
            p1
        }
        catch (e: NumberFormatException) {null}
    }

}