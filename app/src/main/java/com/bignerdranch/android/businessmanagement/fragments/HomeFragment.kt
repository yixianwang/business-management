package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
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

        viewModel.fetchAllHousesList()

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

        // tables
        viewModel.observeAllHouseList().observe(viewLifecycleOwner) {
//            Log.d(javaClass.simpleName, "xxx all house list ${it}")
        }



        val ll = binding.tlHome
        viewModel.observeHomeTableData().observe(viewLifecycleOwner) {
//            Log.d(javaClass.simpleName, "${it}")
            for (i in 0 .. it.size - 1) {
                val row = TableRow(context)
                row.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                )

                val tv1 = TextView(context)
                val tv2 = TextView(context)
                val tv3 = TextView(context)

                tv1.text = it[i].houseId
                tv2.text = it[i].isUnderContractRightNow
                tv3.text = it[i].upcomingAppointment


                tv1.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    2f
                )

                tv2.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    4f
                )

                tv3.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    4f
                )

                tv1.gravity = Gravity.CENTER_HORIZONTAL
                tv2.gravity = Gravity.CENTER_HORIZONTAL
                tv3.gravity = Gravity.CENTER_HORIZONTAL

                row.addView(tv1)
                row.addView(tv2)
                row.addView(tv3)

                ll.addView(row, -1)
            }
        }

        viewModel.observeSwitchMapLiveData().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "xxx ${it}")
        }

    }
}