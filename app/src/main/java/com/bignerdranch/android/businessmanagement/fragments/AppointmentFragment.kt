package com.bignerdranch.android.businessmanagement.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAppointmentBinding
import com.bignerdranch.android.businessmanagement.fragments.adapters.AppointmentAdapter
import com.bignerdranch.android.businessmanagement.fragments.managers.AddAppointmentManager

class AppointmentFragment : Fragment(R.layout.fragment_appointment) {
    companion object {
        fun newInstance() : AppointmentFragment {
            return AppointmentFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentAppointmentBinding? = null

    private val binding get() = _binding!!

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchAppointment()
        } else {
            Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppointmentBinding.bind(view)

        val adapter = AppointmentAdapter(viewModel)
        val rv = binding.appointmentRv
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)

        viewModel.observeAppointmentList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "${it}")
            adapter.submitList(it)
        }


        binding.appointmentAddBut.setOnClickListener {
            val addAppointmentIntent = Intent(context, AddAppointmentManager::class.java)
            val myExtra = Bundle()
//            myExtra.putString(playedCountKey, playedCount)
//            myExtra.putString(onLoopModeKey, onLoopMode)
            addAppointmentIntent.putExtras(myExtra)
            resultLauncher.launch(addAppointmentIntent)
//            startActivity(addAppointmentIntent)
        }
    }
}