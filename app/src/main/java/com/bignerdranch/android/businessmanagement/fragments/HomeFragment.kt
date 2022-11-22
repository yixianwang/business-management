package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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

        if (viewModel.observeAccountantList().value == null) {
            binding.tvCurrentAvailableHouses.text = "error"

        }

//        viewModel.observeAccountantList().observe(viewLifecycleOwner) {
//
//        }
    }
}