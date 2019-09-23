package com.example.project_todo.entity

import android.os.Parcel
import android.os.Parcelable
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task(
    @PrimaryKey
    var id: Long = -1,
    var text: String = "",
    var description: String = "",
    var taskCollectionTitle: String = "",
    var creationDate: String = "",
    var completionDate: String = "",
    var deadline: String = "",
    var isCompleted: Boolean = false,
    var priority: Int = 2):
    RealmObject(),
    Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    ) {
    }

    fun copy(task: Task) {
        apply {
            this.text = task.text
            this.description = task.description
            this.taskCollectionTitle = task.taskCollectionTitle
            this.completionDate = task.completionDate
            this.creationDate = task.creationDate
            this.deadline = task.deadline
            this.isCompleted = task.isCompleted
            this.priority = task.priority
        }
    }

    enum class TaskCompletion {
        ALL, TODO, COMPLETED
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(text)
        parcel.writeString(description)
        parcel.writeString(taskCollectionTitle)
        parcel.writeString(creationDate)
        parcel.writeString(completionDate)
        parcel.writeString(deadline)
        parcel.writeByte(if (isCompleted) 1 else 0)
        parcel.writeInt(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        const val PRIMARY_KEY_FIELD_NAME = "id"
        const val TASK_COLLECTION_TITLE_FIELD = "taskCollectionTitle"

        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}