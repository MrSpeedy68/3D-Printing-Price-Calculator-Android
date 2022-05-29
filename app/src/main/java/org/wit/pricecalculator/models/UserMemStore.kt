package org.wit.pricecalculator.models

import com.google.firebase.database.DatabaseReference
import timber.log.Timber.i
import kotlin.collections.ArrayList

internal fun generateRandomIdUser(): Long {
    return System.currentTimeMillis()
}

class UserMemStore : UserStore {

    var users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun initialize() {
        var temp = findAll()
    }

    override fun create(user: UserModel) {
        user.userId = generateRandomIdUser()
        users.add(user)
        logAll()
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { u -> u.userId == user.userId }

        if (foundUser != null) {
            foundUser.userName = user.userName
            foundUser.labourCost = user.labourCost
            foundUser.energyCost = user.energyCost
            foundUser.currency = user.currency
            logAll()
        }
    }

    override fun delete(user: UserModel) {
        var foundUser: UserModel? = users.find { u -> u.userId == user.userId }
        if (foundUser != null) {
            users.remove(foundUser)
        }
    }

    private fun logAll() {
        users.forEach { i("$it") }
    }

}