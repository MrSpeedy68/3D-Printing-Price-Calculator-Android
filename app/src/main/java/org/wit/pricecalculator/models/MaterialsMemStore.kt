package org.wit.pricecalculator.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class MaterialMemStore : MaterialStore {

    val materials = ArrayList<MaterialsModel>()

    override fun findAll(): List<MaterialsModel> {
        return materials
    }

    override fun create(material: MaterialsModel) {
        material.id = getId()
        materials.add(material)
        logAll()
    }

    override fun update(material: MaterialsModel) {
        var foundMaterial: MaterialsModel? = materials.find { m -> m.id == material.id }
        if (foundMaterial != null) {
            foundMaterial.name = material.name
            foundMaterial.type = material.type
            foundMaterial.weight = material.weight
            foundMaterial.price = material.price
            logAll()
        }
    }

    private fun logAll() {
        materials.forEach { i("$it") }
    }
}