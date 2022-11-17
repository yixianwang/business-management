package com.bignerdranch.android.businessmanagement.fragments.managers

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.databinding.ActivityAddAppointmentBinding

class AddAppointmentManager: AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.okButton.setOnClickListener {
            viewModel.addNewAppointment(
                binding.etTitle.text.toString(),
                binding.etLocation.text.toString(),
                binding.etNote.text.toString(),
                binding.etDeposit.text.toString(),
                binding.etName.text.toString(),
                binding.etPhone.text.toString(),
                binding.etStartDate.text.toString()
            )

            Intent().apply {
                setResult(AppCompatActivity.RESULT_OK, this)
            }

            Log.d(javaClass.simpleName, "Added to firebase succeessfully")
            finish()
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