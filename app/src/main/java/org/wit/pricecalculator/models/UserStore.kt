package org.wit.pricecalculator.models

interface UserStore {
    fun find(): UserModel
    fun create(user: UserModel)
    fun update(user: UserModel)
}