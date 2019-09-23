package com.example.project_todo.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TaskCollection(
    @PrimaryKey
    var taskCollectionId: Long = -1,
    var title: String = "",
    var progressValue: Float = 0F,
    var fullValue: Float = 0F): RealmObject() {

    fun getProgression() = ((progressValue / fullValue) * 100)

    companion object {
        const val TASK_COLLECTION_ID_FIELD_NAME = "taskCollectionId"
    }
}