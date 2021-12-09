package org.wit.pricecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.pricecalculator.databinding.CardMaterialBinding
import org.wit.pricecalculator.models.MaterialsModel

interface MaterialListiner {
    fun onMaterialClick(materials: MaterialsModel)
}

class MaterialAdapter constructor(private var materials: List<MaterialsModel>,
                                   private val listener: MaterialListiner) :
    RecyclerView.Adapter<MaterialAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMaterialBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val material = materials[holder.adapterPosition]
        holder.bind(material, listener)
    }

    override fun getItemCount(): Int = materials.size

    class MainHolder(private val binding : CardMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(material: MaterialsModel, listener: MaterialListiner) {
            binding.materialName.text = material.name
            binding.materialType.text = material.type
            binding.materialWeight.text = material.weight.toString()
            binding.materialPrice.text = material.price.toString()
            Picasso.get().load(material.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onMaterialClick(material) }
        }
    }
}