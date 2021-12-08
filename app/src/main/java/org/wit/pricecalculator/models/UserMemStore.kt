package org.wit.pricecalculator.models

import timber.log.Timber.i
import timber.log.Timber.log
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdUser(): Long {
    return Random().nextLong()
}

class UserMemStore : UserStore { //This class will be replaced once Google login is implemented

    var users = ArrayList<UserModel>()

    override fun findAll(): List<UserModel> {
        return users
    }

    override fun create(user: UserModel) {
        user.userId = generateRandomIdUser()
        users.add(user)
        logAll()
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { u -> u.userId == user.userId }

        if(foundUser != null) {
            foundUser.userName = user.userName
            foundUser.labourCost = user.labourCost
            foundUser.energyCost = user.energyCost
            foundUser.currency = user.currency
            logAll()
        }
    }

    private fun logAll() {
        users.forEach { i("$it") }
    }

}