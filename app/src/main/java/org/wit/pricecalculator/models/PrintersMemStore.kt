package org.wit.pricecalculator.models

import timber.log.Timber.i
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdPrinter(): Long {
    return Random().nextLong()
}

class PrintersMemStore : PrinterStore {

    val printers = ArrayList<PrinterModel>()

    override fun findAll(): List<PrinterModel> {
        return printers
    }

    override fun create(printer: PrinterModel) {
        printer.id = generateRandomIdPrinter()
        printers.add(printer)
        logAll()
    }

    override fun update(printer: PrinterModel) {
        var foundPrinter: PrinterModel? = printers.find { p -> p.id == printer.id }
        if (foundPrinter != null) {
            foundPrinter.name = printer.name
            foundPrinter.price = printer.price
            foundPrinter.wattUsage = printer.wattUsage
            foundPrinter.investmentReturn = printer.investmentReturn
            foundPrinter.image = printer.image
            logAll()
        }
    }

    private fun logAll() {
        printers.forEach { i("$it") }
    }
}