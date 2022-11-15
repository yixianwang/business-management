package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAccountantBinding
import com.bignerdranch.android.businessmanagement.databinding.FragmentHomeBinding


class AccountantFragment : Fragment(R.layout.fragment_accountant) {
    companion object {
        fun newInstance() : AccountantFragment {
            return AccountantFragment()
        }
    }

    private var _binding: FragmentAccountantBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountantBinding.bind(view)

    }
}