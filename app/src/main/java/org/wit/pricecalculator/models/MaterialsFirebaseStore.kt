package org.wit.pricecalculator.models

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
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
        var newId = generateRandomIdMaterial()

        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

        val storageReference = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/${newId}")

        var uploadTask = storageReference.putFile(material.image)
        var downloadUri = ""

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageReference.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                downloadUri = task.result.toString()

                i ("+_______________________________")
                i (downloadUri)

                val mat = mapOf<String,Any>(
                    "id" to newId,
                    "name" to material.name,
                    "type" to material.type,
                    "weight" to material.weight,
                    "price" to material.price,
                    "image" to downloadUri
                )

                database.child(material.name).setValue(mat)

                initialize()


            } else {
                // Handle failures
                // ...
            }
        }
    }

    override fun update(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
        val storageReference = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/${material.id}")

        i ("++++++++++++++++++++++++++++")
        i (material.image.toString())
        i (Uri.parse(storageReference.downloadUrl.toString()).toString())

        if (material.image != storageReference.downloadUrl) {
            storageReference.delete()

            var uploadTask = storageReference.putFile(material.image)

            var downloadUri = ""

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageReference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result.toString()

                    i ("+_______________________________")
                    i (downloadUri)

                    val mat = mapOf<String,Any>(
                        "id" to generateRandomIdMaterial(),
                        "name" to material.name,
                        "type" to material.type,
                        "weight" to material.weight,
                        "price" to material.price,
                        "image" to downloadUri
                    )

                    database.get().addOnSuccessListener() {
                        if (it.exists()) {
                            for (m in it.children) {
                                if (m.child("id").value == material.id) {

                                    //database.child(m.child("name").value.toString())
                                    database.child(m.child("name").value.toString()).removeValue()
                                    database.child(material.name).setValue(mat)


                                    initialize()
                                }
                            }
                        }
                    }



                } else {
                    // Handle failures
                    // ...
                }
            }
        }
        else
        {
            val mat = mapOf<String,Any>(
                "id" to generateRandomIdMaterial(),
                "name" to material.name,
                "type" to material.type,
                "weight" to material.weight,
                "price" to material.price,
                "image" to material.image
            )

            database.get().addOnSuccessListener() {
                if (it.exists()) {
                    for (m in it.children) {
                        if (m.child("id").value == material.id) {

                            //database.child(m.child("name").value.toString())
                            database.child(m.child("name").value.toString()).removeValue()
                            database.child(material.name).setValue(mat)


                            initialize()
                        }
                    }
                }
            }
        }






//        val mat = mapOf<String,Any>(
//                    "id" to generateRandomIdMaterial(),
//                    "name" to material.name,
//                    "type" to material.type,
//                    "weight" to material.weight,
//                    "price" to material.price,
//                    "image" to material.image.toString()
//                )
//
//                database.get().addOnSuccessListener() {
//                    if (it.exists()) {
//                        for (m in it.children) {
//                            if (m.child("id").value == material.id) {
//
//                                i (m.child("id").value.toString())
//                                //database.child(m.child("name").value.toString())
//                                database.child(m.child("name").value.toString()).removeValue()
//                                database.child(material.name).setValue(mat)
//
//
//                                initialize()
//                            }
//                        }
//                    }
//                }
    }

    override fun delete(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

        database.child(material.name).removeValue()

        materials.remove(material)
    }

    private fun logAll() {
        materials.forEach { i("$it") }
    }
}