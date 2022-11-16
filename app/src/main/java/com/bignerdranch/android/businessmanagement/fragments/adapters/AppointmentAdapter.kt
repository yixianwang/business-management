package com.bignerdranch.android.businessmanagement.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.databinding.RowAppointmentBinding
import com.bignerdranch.android.businessmanagement.model.Appointment

class AppointmentAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Appointment, AppointmentAdapter.VH>(Diff())  {
    class Diff : DiffUtil.ItemCallback<Appointment>() {
        override fun areItemsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: Appointment, newItem: Appointment): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.title == newItem.title
                    && oldItem.location == newItem.location
                    && oldItem.note == newItem.note
                    && oldItem.deposit == newItem.deposit
                    && oldItem.name == newItem.name
                    && oldItem.phone == newItem.phone
                    && oldItem.startDate == newItem.startDate
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(val rowAppointmentBinding: RowAppointmentBinding) : RecyclerView.ViewHolder(rowAppointmentBinding.root) {
        fun bind(holder: VH, position: Int) {
            val appointment = getItem(position)
            val binding = holder.rowAppointmentBinding
            binding.tvTitle.text = appointment.title
            binding.tvLocation.text = appointment.location
            binding.tvNote.text = appointment.note
            binding.tvDeposit.text = appointment.deposit
            binding.tvName.text = appointment.name
            binding.tvPhone.text = appointment.phone
            binding.tvStartDate.text = appointment.startDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RowAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}