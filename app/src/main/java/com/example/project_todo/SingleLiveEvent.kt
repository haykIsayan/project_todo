package com.example.project_todo

class SingleLiveEvent<T>: LiveEvent<T>() {

    override fun onFinished() { complete() }

}