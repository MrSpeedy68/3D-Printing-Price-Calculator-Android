package org.wit.pricecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.pricecalculator.databinding.CardPrinterBinding
import org.wit.pricecalculator.models.PrinterModel

interface PrinterListiner {
    fun onPrinterClick(printers: PrinterModel)
}

class PrinterAdapter constructor(private var printers: List<PrinterModel>, private val listener: PrinterListiner) :
        RecyclerView.Adapter<PrinterAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPrinterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val printer = printers[holder.adapterPosition]
        holder.bind(printer, listener)
    }

    override fun getItemCount(): Int = printers.size

    class MainHolder(private val binding : CardPrinterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(printers: PrinterModel, listener: PrinterListiner) {
            binding.printerName.text = printers.name
            binding.printerPrice.text = printers.price.toString()
            binding.printerWattUsage.text = printers.wattUsage.toString()
            binding.investReturn.text = printers.investmentReturn.toString()

            binding.root.setOnClickListener { listener.onPrinterClick(printers) }
        }
    }
}