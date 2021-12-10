package org.wit.pricecalculator.models

interface TaskStore {
    fun findAll(): List<TaskModel>
    fun create(task: TaskModel)
    fun update(task: TaskModel)
    fun delete(task: TaskModel)
}