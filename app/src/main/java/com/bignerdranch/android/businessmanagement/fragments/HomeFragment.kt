package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.bignerdranch.android.businessmanagement.MainActivity
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentCurrentBinding
import com.bignerdranch.android.businessmanagement.databinding.FragmentDataBinding
import com.bignerdranch.android.businessmanagement.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    companion object {
        fun newInstance() : HomeFragment {
            return HomeFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
//        binding.etManageHouse.inputType = InputType.TYPE_NULL


        viewModel.observeHomeTableData().observe(viewLifecycleOwner) {
//            Log.d(javaClass.simpleName, "${it}")
        }

        viewModel.observeAllHouseList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "xxx all houses: ${it}")
        }



        // buttons
        binding.btnManageHouse.setOnClickListener {
            binding.homeTopLayout.isVisible = !binding.homeTopLayout.isVisible
        }

        binding.butAdd.setOnClickListener {
//            Log.d(javaClass.simpleName, "xxx ${binding.etManageHouse.text}")
            if (TextUtils.isEmpty(binding.etManageHouse.text.toString())) {
                Toast.makeText(context, "HouseID Cannot Be Empty", Toast.LENGTH_LONG).show()
            } else {
                viewModel.insertHouse(binding.etManageHouse.text.toString())
            }
        }

        binding.butRemove.setOnClickListener {
            viewModel.eraseHouse(binding.etManageHouse.text.toString())
        }
    }
}