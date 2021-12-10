package org.wit.pricecalculator.models

import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

internal fun generateRandomIdTask(): Long {
    return Random().nextLong()
}

class TaskMemStore : TaskStore {

    var tasks = ArrayList<TaskModel>()

    override fun findAll(): List<TaskModel> {
        return tasks
    }

    override fun create(task: TaskModel) {
        task.id = generateRandomIdUser()
        tasks.add(task)
        logAll()
    }

    override fun update(task: TaskModel) {
        var foundTask: TaskModel? = tasks.find { t -> t.id == task.id }

        if(foundTask != null) {

            logAll()
        }
    }

    override fun delete(task: TaskModel) {
        var foundTask: TaskModel? = tasks.find { t -> t.id == task.id }
        if (foundTask != null) {
            tasks.remove(foundTask)
        }
    }

    private fun logAll() {
        tasks.forEach { Timber.i("$it") }
    }
}