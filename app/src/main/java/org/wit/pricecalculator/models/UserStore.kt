package org.wit.pricecalculator.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
}