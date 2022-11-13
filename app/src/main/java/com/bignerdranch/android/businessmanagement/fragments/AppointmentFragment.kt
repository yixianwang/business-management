package com.bignerdranch.android.businessmanagement.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.businessmanagement.R

class AppointmentFragment : Fragment(R.layout.fragment_appointment) {
    companion object {
        fun newInstance() : AppointmentFragment {
            return AppointmentFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}