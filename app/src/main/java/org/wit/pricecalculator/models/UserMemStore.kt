package org.wit.pricecalculator.models

import timber.log.Timber.i
import timber.log.Timber.log
import java.util.*

class UserMemStore : UserStore { //This class will be replaced once Google login is implemented

    var mainUser = UserModel()

    override fun find(): UserModel {
        return mainUser
    }

    override fun create(user: UserModel) {
        mainUser = user
        logAll()
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel = mainUser

        if(foundUser != null) {
            foundUser.userName = user.userName
            foundUser.labourCost = user.labourCost
            foundUser.energyCost = user.energyCost
            foundUser.currency = user.currency
            logAll()
        }
    }

    private fun logAll() {

    }

}