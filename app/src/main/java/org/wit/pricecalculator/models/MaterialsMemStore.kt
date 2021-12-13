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
    return Random().nextLong()
}

class MaterialMemStore : MaterialStore {


    private lateinit var database: DatabaseReference

    val materials = ArrayList<MaterialsModel>()

    override fun findAll(): List<MaterialsModel> {
        val dbmats = ArrayList<MaterialsModel>()

        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

        database.get().addOnSuccessListener() {
            if(it.exists()) {
                for (m in it.children) {
                    val mat = MaterialsModel(m.child("id").value.toString().toLong(),
                        m.child("name").value.toString(),
                        m.child("type").value.toString(),
                        m.child("weight").value.toString().toInt(),
                        m.child("price").value.toString().toFloat())
                        dbmats.add(mat)

                }

            }


        }

        //i (database.child("Esun Black").child("name").get().toString())

        //i (database.child("Esun Blue").child("type").get().toString())
        return dbmats
    }

    override fun create(material: MaterialsModel) {
//        material.id = generateRandomIdMaterial()
//        materials.add(material)
//        logAll()
        //database = Firebase.database("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Materials")

        database.child(material.name).child("name").setValue(material.name)
        database.child(material.name).child("id").setValue(material.id)
        database.child(material.name).child("type").setValue(material.type)
        database.child(material.name).child("weight").setValue(material.weight)
        database.child(material.name).child("price").setValue(material.price)

        //                Toast.makeText(this, "Successfully Saved Material", Toast.LENGTH_SHORT).show()
//
//                setResult(RESULT_OK)
//                finish()
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failed to Save Material", Toast.LENGTH_SHORT).show()
//            }

    }

    override fun update(material: MaterialsModel) {
        var foundMaterial: MaterialsModel? = materials.find { m -> m.id == material.id }
        if (foundMaterial != null) {
            foundMaterial.name = material.name
            foundMaterial.type = material.type
            foundMaterial.weight = material.weight
            foundMaterial.price = material.price
            foundMaterial.image = material.image
            logAll()
        }
    }

    override fun delete(material: MaterialsModel) {
        var foundMaterial: MaterialsModel? = materials.find { m -> m.id == material.id }
        if (foundMaterial != null) {
            materials.remove(foundMaterial)
        }



    }

    private fun logAll() {
        materials.forEach { i("$it") }
    }
}