package org.wit.pricecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.pricecalculator.databinding.CardPlacemarkBinding
import org.wit.pricecalculator.models.MaterialsModel

interface MaterialListiner {
    fun onPlacemarkClick(materials: MaterialsModel)
}

class MaterialAdapter constructor(private var materials: List<MaterialsModel>,
                                   private val listener: MaterialListiner) :
    RecyclerView.Adapter<MaterialAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlacemarkBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = materials[holder.adapterPosition]
        holder.bind(placemark, listener)
    }

    override fun getItemCount(): Int = materials.size

    class MainHolder(private val binding : CardPlacemarkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(materials: MaterialsModel, listener: MaterialListiner) {
            binding.materialName.text = materials.name
            binding.materialType.text = materials.type
            binding.materialWeight.text = materials.weight.toString()
            binding.materialPrice.text = materials.price.toString()

            binding.root.setOnClickListener { listener.onPlacemarkClick(materials) }
        }
    }
}