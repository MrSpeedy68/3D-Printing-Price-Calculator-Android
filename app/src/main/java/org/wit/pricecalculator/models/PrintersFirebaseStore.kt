package org.wit.pricecalculator.models

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber.i
import kotlin.collections.ArrayList

//internal fun generateRandomIdPrinter(): Long {
//    return System.currentTimeMillis()
//}

class PrintersFirebaseStore : PrinterStore {

    private lateinit var database: DatabaseReference

    val printers = ArrayList<PrinterModel>()

    override fun findAll(): List<PrinterModel> {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        database.get().addOnSuccessListener() {
            printers.clear()
            if(it.exists()) {
                for (p in it.children) {
                    val prntr = PrinterModel(p.child("id").value.toString().toLong(),
                        p.child("name").value.toString(),
                        p.child("price").value.toString().toFloat(),
                        p.child("wattusage").value.toString().toInt(),
                        p.child("investmentreturn").value.toString().toInt(),
                        Uri.parse(p.child("image").value.toString()))
                        printers.add(prntr)
                }
            }
        }
        return printers
    }

    override fun initialize() {
        var temp = findAll()
    }

    override fun create(printer: PrinterModel) {
        var newId = generateRandomIdPrinter()

        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        val storageReference = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/${newId}")

        var uploadTask = storageReference.putFile(printer.image)
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

                val prntr = mapOf<String,Any>(
                    "id" to newId,
                    "name" to printer.name,
                    "price" to printer.price,
                    "wattusage" to printer.wattUsage,
                    "investmentreturn" to printer.investmentReturn,
                    "image" to downloadUri
                )

                database.child(printer.name).setValue(prntr)

                initialize()

            } else {
                // Handle failures
                // ...
            }
        }
    }

    override fun update(printer: PrinterModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        val storageReference = FirebaseStorage.getInstance("gs://d-printing-price-calculator.appspot.com").getReference("images/${printer.id}")

        var uploadTask = storageReference.putFile(printer.image)
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


                val prntr = mapOf<String,Any>(
                    "id" to printer.id,
                    "name" to printer.name,
                    "price" to printer.price,
                    "wattusage" to printer.wattUsage,
                    "investmentreturn" to printer.investmentReturn,
                    "image" to downloadUri
                )

                database.get().addOnSuccessListener() {
                    if (it.exists()) {
                        for (p in it.children) {
                            if (p.child("id").value == printer.id) {

                                //database.child(m.child("name").value.toString())
                                database.child(p.child("name").value.toString()).removeValue()
                                database.child(printer.name).setValue(prntr)

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

    override fun delete(printer: PrinterModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        database.child(printer.name).removeValue()

        printers.remove(printer)
    }

    private fun logAll() {
        printers.forEach { i("$it") }
    }
}