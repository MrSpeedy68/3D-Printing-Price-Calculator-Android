package org.wit.pricecalculator.models

import timber.log.Timber.i
import kotlin.collections.ArrayList

internal fun generateRandomIdMaterial(): Long {
    return System.currentTimeMillis()
}

class MaterialsMemStore : MaterialStore {

    var materials = ArrayList<MaterialsModel>()


    override fun findAll(): List<MaterialsModel> {
        return materials;
    }

    override fun initialize() {
        var temp = findAll()
    }

    override fun create(material: MaterialsModel) {
        material.id = generateRandomIdMaterial()
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
            foundMaterial.image = material.image
            logAll()
        }
    }

    override fun delete(material: MaterialsModel) {
        var foundMaterial: MaterialsModel? = materials.find { m -> m.id == material.id }
        if (foundMaterial != null) {
            materials.remove(foundMaterial)
        }
    }

    private fun logAll() {
        materials.forEach { i("$it") }
    }
}