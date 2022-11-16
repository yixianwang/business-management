package com.bignerdranch.android.businessmanagement.fragments.managers

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
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
            when {
                TextUtils.isEmpty(binding.etTitle.text.toString()) -> Toast.makeText(this, "You must input title", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etLocation.text.toString()) -> Toast.makeText(this, "You must input location", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etRent.text.toString()) -> Toast.makeText(this, "You must input rent", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty("${binding.etStartDate.month + 1}/${binding.etStartDate.dayOfMonth}/${binding.etStartDate.year}") -> Toast.makeText(this, "You must input start date", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty("${binding.etEndDate.month + 1}/${binding.etEndDate.dayOfMonth}/${binding.etEndDate.year}") -> Toast.makeText(this, "You must input start date", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etDuration.text.toString()) -> Toast.makeText(this, "You must input duration", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etNote.text.toString()) -> Toast.makeText(this, "You must input note", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etName.text.toString()) -> Toast.makeText(this, "You must input name", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etPhone.text.toString()) -> Toast.makeText(this, "You must input phone", Toast.LENGTH_SHORT).show()

                else -> {
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
            }
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }


    }
}