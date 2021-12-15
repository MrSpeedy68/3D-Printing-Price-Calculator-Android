package org.wit.pricecalculator.models

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber.i
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdMaterial(): Long {
    return System.currentTimeMillis()
}

class MaterialMemStore : MaterialStore {


    private lateinit var database: DatabaseReference

    var materials = ArrayList<MaterialsModel>()

    override fun findAll(): List<MaterialsModel> {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")


        database.get().addOnSuccessListener() {
            materials.clear()
            if(it.exists()) {
                for (m in it.children) {
//                    val storageRef = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/")
//
//                    val localFile = File.createTempFile(m.child("name").value.toString(),"jpg")
//                    var newImg: Uri = Uri.EMPTY
//                    storageRef.getFile(localFile).addOnSuccessListener {
//                        //val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                        newImg = Uri.parse(localFile.absolutePath)
//                    }

                    val mat = MaterialsModel(m.child("id").value.toString().toLong(),
                        m.child("name").value.toString(),
                        m.child("type").value.toString(),
                        m.child("weight").value.toString().toInt(),
                        m.child("price").value.toString().toFloat(),
                        Uri.parse(m.child("image").value.toString()))
                        materials.add(mat)
                }
            }
        }
        return materials
    }

    override fun initialize() {
        var temp = findAll()
    }

    override fun create(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

//        var newImage = ""
//        val storageReference = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/${material.name}")
//        storageReference.putFile(material.image).addOnSuccessListener {
//            storageReference.downloadUrl.addOnSuccessListener {
//                newImage = it.toString()
//            }
//        }

        val mat = mapOf<String,Any>(
            "id" to generateRandomIdMaterial(),
            "name" to material.name,
            "type" to material.type,
            "weight" to material.weight,
            "price" to material.price,
            "image" to material.image.toString()
            //"image" to material.image.toString()
        )



        database.child(material.name).setValue(mat)


        initialize()
    }

    override fun update(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
        val mat = mapOf<String,Any>(
            "id" to material.id,
            "name" to material.name,
            "type" to material.type,
            "weight" to material.weight,
            "price" to material.price,
            "image" to material.image.toString()
        )

        database.get().addOnSuccessListener() {
            if (it.exists()) {
                for (m in it.children) {
                    if (m.child("id").value == material.id) {

                        //database.child(m.child("name").value.toString())
                        database.child(m.child("name").value.toString()).removeValue()
                        database.child(material.name).setValue(mat)

                    }
                }
            }
        }
        initialize()
    }

    override fun delete(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

        database.child(material.name).removeValue()

        initialize()
    }

    private fun logAll() {
        materials.forEach { i("$it") }
    }
}