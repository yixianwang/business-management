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
import com.bignerdranch.android.businessmanagement.model.Contract
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*


class DataFragment : Fragment(R.layout.fragment_data) {
    companion object {
        fun newInstance() : DataFragment {
            return DataFragment()
        }
    }

    private val rnd = Random()

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentDataBinding? = null

    private val binding get() = _binding!!

    //initializing colors for the entries
    private val colors: ArrayList<Int> = arrayListOf(
        Color.parseColor("#8b7765"),
        Color.parseColor("#ffa500"),
        Color.parseColor("#2E3F62"),
        Color.parseColor("#056F57"),
        Color.parseColor("#012731"),
        Color.parseColor("#278A5B"),
        Color.parseColor("#092256"),
        Color.parseColor("#373021"),
        Color.parseColor("#151F4C"),
        Color.parseColor("#242A1D"),
        Color.parseColor("#1560BD"),
        Color.parseColor("#15736B"),
        Color.parseColor("#1C1E13"),
        Color.parseColor("#1C7C7D"),
        Color.parseColor("#0C1911"),
        Color.parseColor("#240C02"),
        Color.parseColor("#242E16"),
        Color.parseColor("#27504B"),
        Color.parseColor("#2D383A"),
        Color.parseColor("#304B6A"),
        Color.parseColor("#325D52"),
        Color.parseColor("#371D09"),
        Color.parseColor("#37290E"),
        Color.parseColor("#000000"),
        Color.parseColor("#704F50"),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDataBinding.bind(view)

        viewModel.observeContractList().observe(viewLifecycleOwner) {
            setLineChartData(it)
            setPieChart(it)
            setBarChart(it)
        }
    }

    private fun <T, K> Grouping<T, K>.eachSumBy(
        selector: (T) -> Int
    ): Map<K, Int> =
        fold(0) { acc, elem -> acc + selector(elem) }

    private fun setLineChartData(contractList: List<Contract>) {
        val titleList = contractList
            .groupingBy { it.title }
            .eachSumBy { it.rent.toInt() }
            .keys.toList()
        Log.d(javaClass.simpleName, "titleList ${titleList}")

        val data = LineData()
        for (i in titleList.indices) {
            Log.d(javaClass.simpleName, "titleList ${i} ${titleList[i]}")
            val myEachLineData = contractList
                .filter { it.title.toInt() == titleList[i].toInt() }
                .filter { it.s_year.toInt() == MainViewModel.currentYear }
                .groupingBy { it.s_month }
                .eachSumBy { it.rent.toInt() }
                .toList().sortedBy { (key, value) -> key}.toMap()

            Log.d(javaClass.simpleName, "myEachLineData ${myEachLineData}")

            val lineEntry = ArrayList<Entry>()
            for ((key, value) in myEachLineData) {
                Log.d(javaClass.simpleName, "key-value ${key} ${value}")
                lineEntry.add(Entry(key.toFloat(), value.toFloat()))
            }

            val lineDataSet = LineDataSet(lineEntry, "House#${titleList[i]}")
//            lineDataSet.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            lineDataSet.color = colors.get(i)

            Log.d(javaClass.simpleName, "lineDataSet ${lineDataSet}")

//            lineDataSet.color = Color.parseColor("#a35567")
            data.addDataSet(lineDataSet)
        }

        binding.lineChart.data = data
        binding.lineChart.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        binding.lineChart.xAxis.granularity = 1f
//        binding.lineChart.animateXY(3000, 3000)

//        val xAxisLabels = listOf("a", "b", "c", "d", "e")
//        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
//        binding.lineChart.lineData.setValueFormatter(IndexAxisValueFormatter(xAxisLabels))

//        Log.d(javaClass.simpleName, "${}")
        binding.lineChart.description.text = "Monthly Income"
    }



    private fun setPieChart(contractList: List<Contract>) {
        val myPieData = contractList
            .filter { it.s_year.toInt() == MainViewModel.currentYear }
            .groupingBy { it.title }
            .eachSumBy { it.rent.toInt() }
        Log.d(javaClass.simpleName, "myPieData ${myPieData}")

        val label = ""

        val pieEntries: ArrayList<PieEntry> = ArrayList()
        for (key in myPieData.keys) {
            pieEntries.add(PieEntry(myPieData[key]!!.toFloat(), "House#${key}"))
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

    private fun setBarChart(contractList: List<Contract>) {
        val myBarData = contractList
            .filter { it.s_year.toInt() == MainViewModel.currentYear }
            .groupingBy { it.s_month }
            .eachSumBy { it.rent.toInt() }
            .toList().sortedBy { (key, value) -> key}.toMap()
        Log.d(javaClass.simpleName, "myBarData ${myBarData}")

        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "Monthly Income In Total"

//        var xAxisLabels = mutableListOf<String>()
        for ((month, sum) in myBarData) {
            val barEntry = BarEntry(month.toFloat(), sum.toFloat())
            entries.add(barEntry)
//            xAxisLabels.add("${month}")
        }

//        Log.d(javaClass.simpleName, "month ${xAxisLabels}")

        val barDataSet = BarDataSet(entries, title)

        val data = BarData(barDataSet)
        binding.barChart.data = data
        binding.barChart.invalidate()
        binding.barChart.description.text = ""

        val xAxis: XAxis = binding.barChart.getXAxis()
        binding.barChart.xAxis.granularity = 1f
        xAxis.position = XAxisPosition.TOP
        xAxis.textSize = 10f
//        xAxis.textColor = Color.RED
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(true)

    }
}


