package com.selfawarelab.workouttracker.rest

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.selfawarelab.workouttracker.MainViewModel
import com.selfawarelab.workouttracker.R
import kotlinx.android.synthetic.main.fragment_rest.*

class RestFragment : Fragment() {
    val adapter = RestAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restRV.layoutManager = LinearLayoutManager(context)
        restRV.adapter = adapter
        adapter.setDataList(viewModel.getRestData())

        done.setOnClickListener { findNavController().popBackStack() }
    }
}