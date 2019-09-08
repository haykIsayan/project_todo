package com.example.project_todo

class SingleLiveEvent<T>: LiveEvent<T>() {
    fun onEventFinished() { complete() }
}