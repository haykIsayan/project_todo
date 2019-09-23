package com.example.project_todo.core

import androidx.annotation.RequiresPermission
import com.example.project_todo.createObjectIncPrimaryKey
import com.example.project_todo.entity.Task
import com.example.project_todo.updateTask
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.exceptions.RealmException
import io.realm.kotlin.delete

open class TaskDataCore(private val realmConfiguration: RealmConfiguration): TaskRepository {

    override suspend fun deleteTask(task: Task) {
        Realm.getDefaultInstance().apply {
            where(Task::class.java)
                .equalTo(Task.PRIMARY_KEY_FIELD_NAME, task.id)
                .findAll().deleteAllFromRealm()
        }
    }

    override suspend fun updateTask(task: Task) {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            return updateTask(task)
        }
    }

    override suspend fun saveTask(task: Task): Long {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            return createObjectIncPrimaryKey(Task::class.java, Task.PRIMARY_KEY_FIELD_NAME).run {
                copy(task)
                this@apply.commitTransaction()
                this.id
            }
        }
    }

    override suspend fun getTasksByListTitle(parentListTitle: String): List<Task> {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            return where(Task::class.java)
                .equalTo(Task.TASK_COLLECTION_TITLE_FIELD, parentListTitle)
                .findAll().run {
                    this@apply.commitTransaction()
                    this@apply.copyFromRealm(this)
                }
        }
    }
}