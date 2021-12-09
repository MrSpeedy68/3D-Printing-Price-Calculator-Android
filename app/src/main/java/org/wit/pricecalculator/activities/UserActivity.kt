package org.wit.pricecalculator.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.pricecalculator.R
import org.wit.pricecalculator.databinding.ActivityUserBinding
import org.wit.pricecalculator.helpers.showImagePicker
import org.wit.pricecalculator.main.MainApp
import org.wit.pricecalculator.models.UserModel
import timber.log.Timber

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    var user = UserModel()
    lateinit var app: MainApp

    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("user_edit")) {
            edit = true
            user = intent.extras?.getParcelable("user_edit")!!

            binding.userName.setText(user.userName)
            binding.labourCost.setText(user.labourCost.toString())
            binding.energyCost.setText(user.energyCost.toString())
            binding.localCurrency.setText(user.currency)

            binding.btnAdd.text = getString(R.string.button_saveUser)


            Picasso.get()
                .load(user.image)
                .into(binding.userImage)

            if(user.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.button_changeImage)
            }
        }

        binding.btnAdd.setOnClickListener() {
            user.userName = binding.userName.text.toString()
            user.labourCost = binding.labourCost.text.toString().toFloat()
            user.energyCost = binding.energyCost.text.toString().toFloat()
            user.currency = binding.localCurrency.text.toString()


            if (user.userName.isEmpty()) {
                Snackbar.make(it, R.string.no_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.users.update(user.copy())
                } else {
                    app.users.create(user.copy())
                }
            }
            Timber.i("add Button Pressed: $user")
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
        menuInflater.inflate(R.menu.menu_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_delete -> { app.users.delete(user) ; finish() }
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
                            user.image = result.data!!.data!!
                            Picasso.get()
                                .load(user.image)
                                .into(binding.userImage)

                            binding.chooseImage.setText(R.string.button_changeImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}