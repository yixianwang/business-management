package com.bignerdranch.android.businessmanagement.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bignerdranch.android.businessmanagement.BuildConfig
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAccountantBinding
import com.bignerdranch.android.businessmanagement.fragments.managers.ViewPDFActivity
import java.io.File


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

//        val builder: StrictMode.VmPolicy.Builder = Builder()
//        StrictMode.setVmPlicy(builder.build())
//        builder.detectFileUriExposure()

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
            if (viewModel.generatePDF(requireContext())) {
                binding.layoutViewAndShareBtn.isVisible = true
            } else {
                Toast.makeText(context, "Click to quick! Please refresh current page to get data", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnViewPdf.setOnClickListener {
            val viewPDFIntent = Intent(context, ViewPDFActivity::class.java)
            startActivity(viewPDFIntent)
        }

        binding.btnSharePdf.setOnClickListener {
            val path = context?.getExternalFilesDir(null)!!.absolutePath.toString() + "/summary.pdf"
            Log.d(javaClass.simpleName, "${path}")
            val file = File(path)
            val uriPath = uriFromFile(requireContext(), file)
            Log.d(javaClass.simpleName, "${uriPath}")

            // check file existance
            if (file.exists()) {
                // plain text
//                val intent = Intent()
//                intent.action = Intent.ACTION_SEND
//                intent.type = "text/plain"
//                intent.putExtra(Intent.EXTRA_TEXT, path)
//                val chooser = Intent.createChooser(intent, "Share using....")
//                startActivity(chooser)

                // pdf
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "application/pdf"
                intent.putExtra(Intent.EXTRA_STREAM, uriPath)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val chooser = Intent.createChooser(intent, "Share using....")
                startActivity(chooser)
            }
        }
    }

    private fun uriFromFile(context: Context, file:File): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        } else {
            return Uri.fromFile(file)
        }
    }
}