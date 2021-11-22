package org.wit.pricecalculator.models

interface MaterialStore {
    fun findAll(): List<MaterialsModel>
    fun create(materials: MaterialsModel)
    fun update(materials: MaterialsModel)
}