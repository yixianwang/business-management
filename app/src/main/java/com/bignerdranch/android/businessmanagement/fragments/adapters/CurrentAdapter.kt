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
                    && oldItem.title == newItem.title
                    && oldItem.location == newItem.location
                    && oldItem.rent == newItem.rent
                    && oldItem.start == newItem.start
                    && oldItem.end == newItem.end
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
            binding.tvTitle.text = contract.title
            binding.tvLocation.text = contract.location
            binding.tvRent.text = contract.rent
            binding.tvStart.text = contract.start
            binding.tvEnd.text = contract.end
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