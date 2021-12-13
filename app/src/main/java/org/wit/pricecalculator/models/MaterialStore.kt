package org.wit.pricecalculator.models

interface MaterialStore {
    fun findAll(): List<MaterialsModel>
    fun create(material: MaterialsModel)
    fun update(material: MaterialsModel)
    fun delete(material: MaterialsModel)
    fun initialize()
}