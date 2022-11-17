package com.bignerdranch.android.businessmanagement.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAccountantBinding
import com.bignerdranch.android.businessmanagement.fragments.managers.AddContractManager
import com.bignerdranch.android.businessmanagement.fragments.managers.ViewPDFActivity


class AccountantFragment : Fragment(R.layout.fragment_accountant) {
    companion object {
        fun newInstance() : AccountantFragment {
            return AccountantFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentAccountantBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountantBinding.bind(view)

        val ll = binding.accountantTable

        viewModel.fetchAccountant()


        viewModel.observeAccountantList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "${it}")
            Log.d(javaClass.simpleName, "${it.first}")
            Log.d(javaClass.simpleName, "${it.second}")
            val keySet = HashSet<String>()
            keySet.addAll(it.first.keys)
            keySet.addAll(it.second.keys)
            Log.d(javaClass.simpleName, "${keySet}")

            for (i in keySet ) {
                val row = TableRow(context)
                row.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                )

                val tv1 = TextView(context)
                val tv2 = TextView(context)
                val tv3 = TextView(context)

                tv1.text = i
                tv2.text = it.first.getOrDefault(i, "0").toString()
                tv3.text = it.second.getOrDefault(i, "0").toString()


                tv1.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    2f
                )

                tv2.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    2f
                )

                tv3.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    2f
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

        binding.btnGeneratePdf.setOnClickListener {
            viewModel.generatePDF(requireContext())
        }

        binding.btnViewPdf.setOnClickListener {
            val viewPDFIntent = Intent(context, ViewPDFActivity::class.java)
            startActivity(viewPDFIntent)
        }

    }

}