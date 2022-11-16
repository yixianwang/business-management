package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAccountantBinding


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

        val ll = binding.accountantTable

        for (i in 0..50) {
            val row = TableRow(context)
            row.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
            )

            val tv1 = TextView(context)
            val tv2 = TextView(context)
            val tv3 = TextView(context)

            tv1.text = "a" + i
            tv2.text = "b" + i
            tv3.text = "c" + i


            tv1.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                4f
            )

            tv2.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    2f
            )

            tv3.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                4f
            )

            tv1.gravity = Gravity.CENTER_HORIZONTAL
            tv2.gravity = Gravity.CENTER_HORIZONTAL
            tv3.gravity = Gravity.CENTER_HORIZONTAL

//            row.addView(tv1, -1, TableLayout.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                4f
//            ))
//            row.addView(tv2, -1, TableLayout.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                2f
//            ))
//            row.addView(tv3, -1, TableLayout.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT,
//                4f
//            ))

            row.addView(tv1)
            row.addView(tv2)
            row.addView(tv3)





//            checkBox = CheckBox(this)
//            tv = TextView(this)
//            addBtn = ImageButton(this)
//            addBtn.setImageResource(R.drawable.add)
//            minusBtn = ImageButton(this)
//            minusBtn.setImageResource(R.drawable.minus)
//            qty = TextView(this)
//            checkBox.setText("hello")
//            qty.setText("10")
//            row.addView(checkBox)
//            row.addView(minusBtn)
//            row.addView(qty)
//            row.addView(addBtn)

            ll.addView(row, -1)
        }
    }
}