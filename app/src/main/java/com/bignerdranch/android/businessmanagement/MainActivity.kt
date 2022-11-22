package com.bignerdranch.android.businessmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.bignerdranch.android.businessmanagement.databinding.ActivityMainBinding
import com.bignerdranch.android.businessmanagement.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel.fetchContract()
        viewModel.fetchAppointment()
        if (viewModel.observeContractList().value != null) {
            viewModel.fetchAccountant()
        }

        super.onCreate(savedInstanceState)
//        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val currentFragment = CurrentFragment.newInstance()
        val appointmentFragment = AppointmentFragment.newInstance()
        val homeFragment = HomeFragment.newInstance()
        val accountantFragment = AccountantFragment.newInstance()
        val dataFragment = DataFragment.newInstance()

        changeFragment(homeFragment)

        val myBottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        myBottomNavigationView.selectedItemId = R.id.ic_home

        myBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_current -> changeFragment(currentFragment)
                R.id.ic_appointment -> changeFragment(appointmentFragment)
                R.id.ic_home -> changeFragment(homeFragment)
                R.id.ic_accountant -> changeFragment(accountantFragment)
                R.id.ic_data -> changeFragment(dataFragment)
            }
            true
        }

    }

    private fun changeFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
}