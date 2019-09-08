package com.example.project_todo.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.example.project_todo.entity.Resource
import com.example.project_todo.viewmodel.MainViewModel


abstract class BaseFragment : Fragment() {

    protected val mMainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(getResourceLayout(), container, false)
    }

    @LayoutRes abstract fun getResourceLayout(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mMainViewModel.getTaskListsData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {

                }

            }
        })
    }


}
