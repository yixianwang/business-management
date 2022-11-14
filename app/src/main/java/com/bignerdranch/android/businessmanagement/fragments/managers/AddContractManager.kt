package com.bignerdranch.android.businessmanagement.fragments.managers

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                binding.etStart.text.toString(),
                binding.etEnd.text.toString(),
                binding.etDuration.text.toString(),
                binding.etNote.text.toString(),
                binding.etName.text.toString(),
                binding.etPhone.text.toString()
            )

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