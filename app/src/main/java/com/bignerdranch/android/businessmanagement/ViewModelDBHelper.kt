package com.bignerdranch.android.businessmanagement

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.businessmanagement.model.Accountant
import com.bignerdranch.android.businessmanagement.model.Appointment
import com.bignerdranch.android.businessmanagement.model.Contract
import com.bignerdranch.android.businessmanagement.model.House
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val allContracts = "allContracts"
    private val allAppointments = "allAppointments"
    private val allHouses = "allHouses"

    // Contract operation
    // https://firebase.google.com/docs/firestore/query-data/order-limit-data
    private fun dbFetchContract(contractList: MutableLiveData<List<Contract>>) {
        db.collection(allContracts)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allContracts fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                contractList.postValue(result.documents.mapNotNull {
                    it.toObject(Contract::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allContracts fetch FAILED ", it)
            }
    }

    fun fetchContract(contractList: MutableLiveData<List<Contract>>) {
        dbFetchContract(contractList)
    }

    // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
    fun createContract(
        contract: Contract,
        contractList: MutableLiveData<List<Contract>>
    ) {
        contract.firestoreID = db.collection(allContracts).document().id

        db.collection(allContracts)
            .add(contract)
            .addOnSuccessListener {
                dbFetchContract(contractList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail create")
            }
    }

    fun removeContract(
        contract: Contract,
        contractList: MutableLiveData<List<Contract>>
    ) {
        db.collection(allContracts)
            .document(contract.firestoreID)
            .delete()
            .addOnSuccessListener {
                dbFetchContract(contractList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail remove")
            }
    }

    // Appointment operations
    private fun dbFetchAppointment(appointmentList: MutableLiveData<List<Appointment>>) {
        db.collection(allAppointments)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allAppointments fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                appointmentList.postValue(result.documents.mapNotNull {
                    it.toObject(Appointment::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allAppointments fetch FAILED ", it)
            }
    }

    fun fetchAppointment(appointmentList: MutableLiveData<List<Appointment>>) {
        dbFetchAppointment(appointmentList)
    }

    fun createAppointment(
        appointment: Appointment,
        appointmentList: MutableLiveData<List<Appointment>>
    ) {
        appointment.firestoreID = db.collection(allAppointments).document().id

        db.collection(allAppointments)
            .add(appointment)
            .addOnSuccessListener {
                dbFetchAppointment(appointmentList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail create")
            }
    }

    fun removeAppointment(
        appointment: Appointment,
        appointmentList: MutableLiveData<List<Appointment>>
    ) {
        db.collection(allAppointments)
            .document(appointment.firestoreID)
            .delete()
            .addOnSuccessListener {
                dbFetchAppointment(appointmentList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail remove")
            }
    }

    // Home Fragment All Houses List
    private fun dbfetchAllHousesList(allHouseList: MutableLiveData<List<House>>) {
        db.collection(allHouses)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allHousesList fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                allHouseList.postValue(result.documents.mapNotNull {
                    it.toObject(House::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allAppointments fetch FAILED ", it)
            }
    }

    fun fetchAllHousesList(allHouseList: MutableLiveData<List<House>>) {
        dbfetchAllHousesList(allHouseList)
    }

    fun createNewHouse(
        house: House,
        allHouseList: MutableLiveData<List<House>>
    ) {
        house.firestoreID = db.collection(allAppointments).document().id

        db.collection(allHouses)
            .add(house)
            .addOnSuccessListener {
                dbfetchAllHousesList(allHouseList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail create")
            }
    }

    fun removeHouse(
        house: House,
        allHouseList: MutableLiveData<List<House>>
    ) {
        db.collection(allHouses)
            .document(house.firestoreID)
            .delete()
            .addOnSuccessListener {
                dbfetchAllHousesList(allHouseList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail remove")
            }
    }
}