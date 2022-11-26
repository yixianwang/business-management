package com.bignerdranch.android.businessmanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.bignerdranch.android.businessmanagement.databinding.ActivityMainBinding
import com.bignerdranch.android.businessmanagement.fragments.*
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
//            viewModel.updateUser()
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        viewModel.fetchContract()
        viewModel.fetchAppointment()
        viewModel.fetchAllHousesList()

        super.onCreate(savedInstanceState)
//        val activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val currentFragment = CurrentFragment.newInstance()
        val appointmentFragment = AppointmentFragment.newInstance()
        val homeFragment = HomeFragment.newInstance()
        val accountantFragment = AccountantFragment.newInstance()
        val dataFragment = DataFragment.newInstance()

//        changeFragment(homeFragment)
        // test acc
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

        AuthInit(viewModel, signInLauncher)



    }

    private fun changeFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.auth_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.itemId

        return if (id == R.id.log_out) {
            settingsButton(item)
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun settingsButton(@Suppress("UNUSED_PARAMETER") item: MenuItem) {
        // XXX Write me
//        Log.d("XXX select but", "")
        viewModel.signOut()
        AuthInit(viewModel, signInLauncher)

    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchContract()
        viewModel.fetchAppointment()
        viewModel.fetchAllHousesList()
    }
}