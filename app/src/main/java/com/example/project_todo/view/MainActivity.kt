package com.example.project_todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.project_todo.R
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar

class MainActivity : AppCompatActivity() {

    private lateinit var ncNavController: NavController
    private lateinit var tvSelectDate: TextView

    private lateinit var babToolbar: BottomAppBar

    private lateinit var mMainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ncNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        if (savedInstanceState == null) {
            ncNavController.navigate(R.id.destination_todos)
        }

        tvSelectDate = findViewById(R.id.tv_select_date_activity_main)
        tvSelectDate.setOnClickListener { selectDate() }

        babToolbar = findViewById(R.id.bab_toolbar_activity_main)
        babToolbar.setNavigationOnClickListener { selectList() }

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mMainViewModel.fetchData()
    }

    private fun selectDate() {

    }

    private fun selectList() {

    }
}
