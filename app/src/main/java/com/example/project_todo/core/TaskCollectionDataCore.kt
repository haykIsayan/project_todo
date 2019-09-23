package com.example.project_todo.core

import com.example.project_todo.Constants
import com.example.project_todo.createObjectIncPrimaryKey
import com.example.project_todo.entity.TaskCollection
import io.realm.Realm
import io.realm.exceptions.RealmException

open class TaskCollectionDataCore: TaskListRepository {
    override suspend fun updateTaskCollection(taskCollection: TaskCollection) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            where(TaskCollection::class.java).findFirst().apply {
                if (this == null) {
                    commitTransaction()
                    throw RealmException(Constants.REALM_UPDATE_EXCEPTION_MESSAGE)
                } else {
                    apply {
                        title = taskCollection.title
                        progressValue = taskCollection.progressValue
                        fullValue = taskCollection.fullValue
                    }
                    commitTransaction()
                }
            }
        }
    }

    override suspend fun getTaskCollections(): List<TaskCollection> {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            return where(TaskCollection::class.java).findAll().run {
                this@apply.copyFromRealm(this).also {
                    this@apply.commitTransaction()
                }
            }
        }
    }

    override suspend fun addTaskCollection(taskCollection: TaskCollection): Long
            = Realm.getDefaultInstance()
        .run {
            beginTransaction()
            createObjectIncPrimaryKey(
                TaskCollection::class.java,
                TaskCollection.TASK_COLLECTION_ID_FIELD_NAME).run {
                commitTransaction()
                taskCollectionId
            }
        }

}