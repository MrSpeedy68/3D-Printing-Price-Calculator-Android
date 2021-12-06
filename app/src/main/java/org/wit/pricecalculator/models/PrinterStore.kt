package org.wit.pricecalculator.models

interface PrinterStore {
    fun findAll(): List<PrinterModel>
    fun create(materials: PrinterModel)
    fun update(materials: PrinterModel)
}