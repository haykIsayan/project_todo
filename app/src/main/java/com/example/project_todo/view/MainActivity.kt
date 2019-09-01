package com.example.project_todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.project_todo.R
import com.example.project_todo.TodoUtils
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar

class MainActivity : AppCompatActivity() {

    private lateinit var ncNavController: NavController

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ncNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mMainViewModel.setListTitle(TodoUtils.Constants.TEST_LIST_TITLE)
    }

    private fun selectList() {

    }
}
