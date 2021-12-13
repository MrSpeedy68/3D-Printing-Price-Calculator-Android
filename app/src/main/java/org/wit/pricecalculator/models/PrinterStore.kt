package org.wit.pricecalculator.models

interface PrinterStore {
    fun findAll(): List<PrinterModel>
    fun create(printer: PrinterModel)
    fun update(printer: PrinterModel)
    fun delete(printer: PrinterModel)
    fun initialize()

}