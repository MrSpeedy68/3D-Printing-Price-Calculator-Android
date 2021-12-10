package org.wit.pricecalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.pricecalculator.databinding.CardTaskBinding
import org.wit.pricecalculator.models.TaskModel


interface TaskListiner {
    fun onTaskClick(tasks: TaskModel)
}

class TaskAdapter constructor(private var tasks: List<TaskModel>, private val listener: TaskListiner) :
    RecyclerView.Adapter<TaskAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardTaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task, listener)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding : CardTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskModel, listener: TaskListiner) {
            binding.customerName.text = task.customerName
            binding.taskDesc.text = task.taskDescription
            binding.address.text = task.address
            binding.taskCost.text = task.taskCost.toString()
            binding.shippingCost.text = task.shippingCost.toString()

            binding.root.setOnClickListener { listener.onTaskClick(task) }
        }
    }
}