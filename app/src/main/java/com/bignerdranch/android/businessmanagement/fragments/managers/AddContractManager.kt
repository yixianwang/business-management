package com.bignerdranch.android.businessmanagement.fragments.managers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.businessmanagement.MainViewModel
import androidx.activity.viewModels
import com.bignerdranch.android.businessmanagement.databinding.ActivityAddContractBinding

class AddContractManager : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddContractBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.okButton.setOnClickListener {
            viewModel.addNewContract(
                binding.etTitle.text.toString(),
                binding.etLocation.text.toString(),
                binding.etRent.text.toString(),
                "${binding.etStartDate.month + 1}/${binding.etStartDate.dayOfMonth}/${binding.etStartDate.year}",
                "${binding.etEndDate.month + 1}/${binding.etEndDate.dayOfMonth}/${binding.etEndDate.year}",
                binding.etDuration.text.toString(),
                binding.etNote.text.toString(),
                binding.etName.text.toString(),
                binding.etPhone.text.toString()
            )

//            Toast.makeText(applicationContext, "${binding.etStartDate.month + 1}/${binding.etStartDate.dayOfMonth}/${binding.etStartDate.year}", Toast.LENGTH_SHORT).show()


            Intent().apply {
                setResult(RESULT_OK, this)
            }

            Log.d(javaClass.simpleName, "Added to firebase succeessfully")
            finish()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }


    }
}