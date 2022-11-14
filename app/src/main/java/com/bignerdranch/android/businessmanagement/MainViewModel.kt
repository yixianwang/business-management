package com.bignerdranch.android.businessmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.businessmanagement.model.Appointment
import com.bignerdranch.android.businessmanagement.model.Contract

class MainViewModel: ViewModel() {
    private var contractList = MutableLiveData<List<Contract>>()
    private var appointmentList = MutableLiveData<List<Appointment>>()
    private val dbHelp = ViewModelDBHelper()

    fun addNewContract(title: String,
                       location: String,
                       rent: String,
                       start: String,
                       end: String,
                       duration: String,
                       note: String,
                       name: String,
                       phone: String) {
        val contract = Contract(
            title = title,
            location = location,
            rent = rent,
            start = start,
            end = end,
            duration = duration,
            note = note,
            name = name,
            phone = phone
        )
        dbHelp.createContract(contract, contractList)
    }

    fun addNewAppointment(title: String,
                          location: String,
                          note: String,
                          deposit: String,
                          name: String,
                          phone: String) {
        val appointment = Appointment(
            title = title,
            location = location,
            deposit = deposit,
            note = note,
            name = name,
            phone = phone
        )
        dbHelp.createAppointment(appointment, appointmentList)
    }

    fun fetchContract() {
        dbHelp.fetchContract(contractList)
    }

    fun fetchAppointment() {
        dbHelp.fetchAppointment(appointmentList)
    }

    fun observeContractList(): LiveData<List<Contract>> {
        return contractList
    }

    fun observeAppointmentList(): LiveData<List<Appointment>> {
        return appointmentList
    }

}