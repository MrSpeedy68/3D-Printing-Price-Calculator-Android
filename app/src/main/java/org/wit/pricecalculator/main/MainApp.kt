package org.wit.pricecalculator.main

import android.app.Application
import org.wit.pricecalculator.models.MaterialMemStore
import org.wit.pricecalculator.models.PrintersMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val materials = MaterialMemStore()

    val printers = PrintersMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("3DPPC started")
    }
}