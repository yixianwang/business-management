package com.bignerdranch.android.businessmanagement.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentCurrentBinding
import com.bignerdranch.android.businessmanagement.databinding.FragmentDataBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DataFragment : Fragment(R.layout.fragment_data) {
    companion object {
        fun newInstance() : DataFragment {
            return DataFragment()
        }
    }


    private var _binding: FragmentDataBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDataBinding.bind(view)

        setLineChartData()
    }

    fun setLineChartData() {
        val xvalue = ArrayList<String>()
        xvalue.add("11.00 am")
        xvalue.add("12.00 am")
        xvalue.add("1.00 am")
        xvalue.add("3.00 am")
        xvalue.add("5.00 am")

        val lineEntry = ArrayList<Entry>();
        lineEntry.add(Entry(20f, 10f))
        lineEntry.add(Entry(30f, 1f))
        lineEntry.add(Entry(40f, 2f))
        lineEntry.add(Entry(50f, 3f))
        lineEntry.add(Entry(60f, 4f))

        val lineDataSet = LineDataSet(lineEntry, "First")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

//        val data = LineData(xvalue, lineDataSet)
        val data = LineData(lineDataSet)

        binding.lineChart.data = data
        binding.lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
//        binding.lineChart.animateXY(3000, 3000)
    }
}


