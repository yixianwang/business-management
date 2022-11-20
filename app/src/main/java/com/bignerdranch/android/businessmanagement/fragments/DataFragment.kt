package com.bignerdranch.android.businessmanagement.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentDataBinding
import com.bignerdranch.android.businessmanagement.model.Appointment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class DataFragment : Fragment(R.layout.fragment_data) {
    companion object {
        fun newInstance() : DataFragment {
            return DataFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentDataBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDataBinding.bind(view)

        setLineChartData()
        showPieChart()
        setBarChart()
    }

    private fun setLineChartData() {
        viewModel.observeAccountantList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "acc list")
        }

        val lineEntry = ArrayList<Entry>()
        lineEntry.add(Entry(20f, 10f))
        lineEntry.add(Entry(30f, 1f))
        lineEntry.add(Entry(40f, 2f))
        lineEntry.add(Entry(50f, 3f))
        lineEntry.add(Entry(60f, 4f))

        val lineDataSet = LineDataSet(lineEntry, "First")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

        val lineEntry2 = ArrayList<Entry>()
        lineEntry2.add(Entry(20f, 1f))
        lineEntry2.add(Entry(30f, 2f))
        lineEntry2.add(Entry(40f, 3f))
        lineEntry2.add(Entry(50f, 4f))
        lineEntry2.add(Entry(60f, 5f))

        val lineDataSet2 = LineDataSet(lineEntry2, "Second")


        val data = LineData()
        data.addDataSet(lineDataSet)
        data.addDataSet(lineDataSet2)

        binding.lineChart.data = data
        binding.lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
//        binding.lineChart.animateXY(3000, 3000)

//        val xAxisLabels = listOf("a", "b", "c", "d", "e")
//        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//        binding.lineChart.lineData.setValueFormatter(IndexAxisValueFormatter(xAxisLabels))

//        Log.d(javaClass.simpleName, "${}")
        binding.lineChart.description.text = "Monthly Income"
    }



    private fun showPieChart() {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = "type"

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["Toys"] = 200
        typeAmountMap["Snacks"] = 230
        typeAmountMap["Clothes"] = 100
        typeAmountMap["Stationary"] = 500
        typeAmountMap["Phone"] = 50

        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#304567"))
        colors.add(Color.parseColor("#309967"))
        colors.add(Color.parseColor("#476567"))
        colors.add(Color.parseColor("#890567"))
        colors.add(Color.parseColor("#a35567"))
        colors.add(Color.parseColor("#ff5f67"))
        colors.add(Color.parseColor("#3ca567"))

        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()
        binding.pieChart.description.text = ""
    }

    private fun setBarChart() {
        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "Title"

        //input data
        for (i in 0..5) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }

        val barDataSet = BarDataSet(entries, title)

        val data = BarData(barDataSet)
        binding.barChart.data = data
        binding.barChart.invalidate()

        val xAxisLabels = listOf("1a", "2a", "3", "4", "5", "6")
        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
        binding.barChart.description.text = ""
    }
}


