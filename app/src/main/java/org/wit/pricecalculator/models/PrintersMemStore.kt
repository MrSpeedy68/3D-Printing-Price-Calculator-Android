package org.wit.pricecalculator.models

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber.i
import kotlin.collections.ArrayList

internal fun generateRandomIdPrinter(): Long {
    return System.currentTimeMillis()
}

class PrintersMemStore : PrinterStore {

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
                        p.child("investmentreturn").value.toString().toInt())
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
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        val prntr = mapOf<String,Any>(
            "id" to generateRandomIdPrinter(),
            "name" to printer.name,
            "price" to printer.price,
            "wattusage" to printer.wattUsage,
            "investmentreturn" to printer.investmentReturn
        )

        database.child(printer.name).setValue(prntr)


        //                Toast.makeText(this, "Successfully Saved Material", Toast.LENGTH_SHORT).show()
//
//                setResult(RESULT_OK)
//                finish()
//            }.addOnFailureListener {
//                Toast.makeText(this, "Failed to Save Material", Toast.LENGTH_SHORT).show()
//            }

        initialize()
    }

    override fun update(printer: PrinterModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")
        val prntr = mapOf<String,Any>(
            "id" to generateRandomIdPrinter(),
            "name" to printer.name,
            "price" to printer.price,
            "wattusage" to printer.wattUsage,
            "investmentreturn" to printer.investmentReturn
        )

        database.get().addOnSuccessListener() {
            if (it.exists()) {
                for (p in it.children) {
                    if (p.child("id").value == printer.id) {

                        //database.child(m.child("name").value.toString())
                        database.child(printer.name).setValue(prntr)
                        database.child(p.child("name").value.toString()).removeValue()
                    }
                }
            }
        }
        initialize()
    }

    override fun delete(printer: PrinterModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Printers")

        database.child(printer.name).removeValue()

        initialize()
    }

    private fun logAll() {
        printers.forEach { i("$it") }
    }
}