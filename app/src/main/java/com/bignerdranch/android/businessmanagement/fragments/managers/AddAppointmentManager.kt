package com.bignerdranch.android.businessmanagement.fragments.managers

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.databinding.ActivityAddAppointmentBinding

class AddAppointmentManager: AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
//    private val viewModel: MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchAllHousesList()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.okButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.etTitle.text.toString()) -> Toast.makeText(this, "You must input HouseID", Toast.LENGTH_SHORT).show()
                !viewModel.countHouse(binding.etTitle.text.toString()) -> Toast.makeText(this, "Please input valid HouseID. Please Check Home Page", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etLocation.text.toString()) -> Toast.makeText(this, "You must input location", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etNote.text.toString()) -> Toast.makeText(this, "You must input note", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etDeposit.text.toString()) -> Toast.makeText(this, "You must input deposit", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etName.text.toString()) -> Toast.makeText(this, "You must input name", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(binding.etPhone.text.toString()) -> Toast.makeText(this, "You must input phone", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty("${binding.etStartDate.month + 1}/${binding.etStartDate.dayOfMonth}/${binding.etStartDate.year}") -> Toast.makeText(this, "You must input start date", Toast.LENGTH_SHORT).show()

                else -> {
                    viewModel.addNewAppointment(
                        binding.etTitle.text.toString(),
                        binding.etLocation.text.toString(),
                        binding.etNote.text.toString(),
                        binding.etDeposit.text.toString(),
                        binding.etName.text.toString(),
                        binding.etPhone.text.toString(),
                        binding.etStartDate.year.toString(),
                        "${binding.etStartDate.month + 1}",
                        "${binding.etStartDate.dayOfMonth}",
                    )

                    Intent().apply {
                        setResult(AppCompatActivity.RESULT_OK, this)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.home -> finish()
        }
        return true
    }
}