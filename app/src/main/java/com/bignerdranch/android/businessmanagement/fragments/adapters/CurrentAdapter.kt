package com.bignerdranch.android.businessmanagement.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.databinding.RowCurrentBinding
import com.bignerdranch.android.businessmanagement.model.Contract

class CurrentAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Contract, CurrentAdapter.VH>(Diff()) {
    class Diff : DiffUtil.ItemCallback<Contract>() {
        override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.houseID == newItem.houseID
                    && oldItem.location == newItem.location
                    && oldItem.rent == newItem.rent
                    && oldItem.s_year == newItem.s_year
                    && oldItem.s_month == newItem.s_month
                    && oldItem.s_date == newItem.s_date
                    && oldItem.e_year == newItem.e_year
                    && oldItem.e_month == newItem.e_month
                    && oldItem.e_date == newItem.e_date
                    && oldItem.duration == newItem.duration
                    && oldItem.note == newItem.note
                    && oldItem.name == newItem.name
                    && oldItem.phone == newItem.phone
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(val rowCurrentBinding: RowCurrentBinding) : RecyclerView.ViewHolder(rowCurrentBinding.root) {
        fun bind(holder: VH, position: Int) {
            val contract = getItem(position)
            val binding = holder.rowCurrentBinding
            binding.tvHouseId.text = contract.houseID
            binding.tvLocation.text = contract.location
            binding.tvRent.text = contract.rent
            binding.tvStart.text = contract.s_month + "/" + contract.s_date + "/" + contract.s_year
            binding.tvEnd.text = contract.e_month + "/" + contract.e_date + "/" + contract.e_year
            binding.tvDuration.text = contract.duration
            binding.tvNote.text = contract.note
            binding.tvName.text = contract.name
            binding.tvPhone.text = contract.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(RowCurrentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}