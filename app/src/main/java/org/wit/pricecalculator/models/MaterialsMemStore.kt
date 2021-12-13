package org.wit.pricecalculator.models

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber.i
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
                        m.child("price").value.toString().toFloat())
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

        val mat = mapOf<String,Any>(
            "id" to generateRandomIdMaterial(),
            "name" to material.name,
            "type" to material.type,
            "weight" to material.weight,
            "price" to material.price
        )

        database.child(material.name).setValue(mat)


        //                Toast.makeText(this, "Successfully Saved Material", Toast.LENGTH_SHORT).show()
//
//                setResult(RESULT_OK)
//                finish()
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failed to Save Material", Toast.LENGTH_SHORT).show()
//            }

        initialize()
    }

    override fun update(material: MaterialsModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
        val mat = mapOf<String,Any>(
            "id" to material.id,
            "name" to material.name,
            "type" to material.type,
            "weight" to material.weight,
            "price" to material.price
        )

        database.get().addOnSuccessListener() {
            if (it.exists()) {
                for (m in it.children) {
                    if (m.child("id").value == material.id) {

                        //database.child(m.child("name").value.toString())
                        database.child(material.name).setValue(mat)
                        database.child(m.child("name").value.toString()).removeValue()
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