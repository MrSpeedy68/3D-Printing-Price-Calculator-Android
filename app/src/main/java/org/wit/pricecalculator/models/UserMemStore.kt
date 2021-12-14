package org.wit.pricecalculator.models

import android.net.Uri
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber.i
import timber.log.Timber.log
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdUser(): Long {
    return System.currentTimeMillis()
}

class UserMemStore : UserStore { //This class will be replaced once Google login is implemented

    private lateinit var database: DatabaseReference

    var users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        database.get().addOnSuccessListener() {
            users.clear()
            if(it.exists()) {
                for (u in it.children) {
                    val usr = UserModel(u.child("id").value.toString().toLong(),
                        u.child("name").value.toString(),
                        u.child("labourcost").value.toString().toFloat(),
                        u.child("energycost").value.toString().toFloat(),
                        u.child("currency").value.toString(),
                        Uri.parse(u.child("image").value.toString()) )

                        users.add(usr)
                }
            }
        }
        return users
    }

    override fun initialize() {
        var temp = findAll()
    }

    override fun create(user: UserModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        val usr = mapOf<String,Any>(
            "id" to generateRandomIdUser(),
            "name" to user.userName,
            "labourcost" to user.labourCost,
            "energycost" to user.energyCost,
            "currency" to user.currency,
            "image" to user.image.toString()
        )

        database.child(user.userName).setValue(usr)


        initialize()
    }

    override fun update(user: UserModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
        val usr = mapOf<String,Any>(
            "id" to user.userId,
            "name" to user.userName,
            "labourcost" to user.labourCost,
            "energycost" to user.energyCost,
            "currency" to user.currency,
            "image" to user.image.toString()
        )

        database.get().addOnSuccessListener() {
            if (it.exists()) {
                for (u in it.children) {
                    if (u.child("id").value == user.userId) {

                        //database.child(m.child("name").value.toString())
                        database.child(u.child("name").value.toString()).removeValue()
                        database.child(user.userName).setValue(usr)

                    }
                }
            }
        }
        initialize()
    }

    override fun delete(user: UserModel) {
        database = FirebaseDatabase.getInstance("https://d-printing-price-calculator-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")

        database.child(user.userName).removeValue()

        initialize()
    }

    private fun logAll() {
        users.forEach { i("$it") }
    }

}