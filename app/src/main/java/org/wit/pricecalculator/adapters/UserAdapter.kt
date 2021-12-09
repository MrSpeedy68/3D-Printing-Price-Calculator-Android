package org.wit.pricecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.pricecalculator.databinding.CardUserBinding
import org.wit.pricecalculator.models.UserModel

interface UserListiner {
    fun onUserClick(users: UserModel)
}

class UserAdapter constructor(private var users: List<UserModel>, private val listener: UserListiner) :
        RecyclerView.Adapter<UserAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val user = users[holder.adapterPosition]
        holder.bind(user, listener)
    }

    override fun getItemCount(): Int = users.size

    class MainHolder(private val binding : CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserModel, listener: UserListiner) {
            binding.userName.text = user.userName
            binding.labourCost.text = user.labourCost.toString()
            binding.energyCost.text = user.energyCost.toString()
            binding.localCurrency.text = user.currency
            Picasso.get().load(user.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onUserClick(user) }
        }
    }
}