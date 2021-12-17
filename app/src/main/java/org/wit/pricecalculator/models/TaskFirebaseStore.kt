package org.wit.pricecalculator.models

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber
import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdTask(): Long {
    return System.currentTimeMillis()
}

class TaskMemStore : TaskStore {

    private lateinit var database: DatabaseReference

    var tasks = ArrayList<TaskModel>()

    override fun findAll(): List<TaskModel> {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Tasks")

        database.get().addOnSuccessListener() {
            tasks.clear()
            if(it.exists()) {
                for (t in it.children) {
                    val tsk = TaskModel(t.child("id").value.toString().toLong(),
                        t.child("name").value.toString(),
                        t.child("description").value.toString(),
                        t.child("address").value.toString(),
                        t.child("taskcost").value.toString().toFloat(),
                        t.child("shippingcost").value.toString().toFloat(),
                        t.child("lat").value.toString().toDouble(),
                        t.child("lng").value.toString().toDouble(),
                        t.child("zoom").value.toString().toFloat())
                    tasks.add(tsk)
                }
            }
        }
        return tasks
    }

    override fun initialize() {
        var temp = findAll()
    }


    override fun create(task: TaskModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Tasks")

        val tsk = mapOf<String,Any>(
            "id" to generateRandomIdMaterial(),
            "name" to task.customerName,
            "description" to task.taskDescription,
            "address" to task.address,
            "taskcost" to task.taskCost,
            "shippingcost" to task.shippingCost,
            "lat" to task.lat,
            "lng" to task.lng,
            "zoom" to task.zoom
        )

        database.child(task.customerName).setValue(tsk)

        initialize()
    }

    override fun update(task: TaskModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Tasks")
        val tsk = mapOf<String,Any>(
            "id" to generateRandomIdMaterial(),
            "name" to task.customerName,
            "description" to task.taskDescription,
            "address" to task.address,
            "taskcost" to task.taskCost,
            "shippingcost" to task.shippingCost,
            "lat" to task.lat,
            "lng" to task.lng,
            "zoom" to task.zoom
        )

        i ("Entered Update")

        database.get().addOnSuccessListener() {
            if (it.exists()) {
                for (t in it.children) {
                    if (t.child("id").value == task.id) {


                        i ("Entered deleting")
                        //database.child(m.child("name").value.toString())
                        database.child(t.child("name").value.toString()).removeValue()
                        database.child(task.customerName).setValue(tsk)

                        initialize()
                    }
                }
            }
        }

    }

    override fun delete(task: TaskModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Tasks")

        database.child(task.customerName).removeValue()

        tasks.remove(task)
    }

    private fun logAll() {
        tasks.forEach { Timber.i("$it") }
    }
}