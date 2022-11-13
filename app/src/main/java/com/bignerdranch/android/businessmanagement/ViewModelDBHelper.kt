package com.bignerdranch.android.businessmanagement

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.businessmanagement.model.Contract
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val allContracts = "allContracts"
    private val allAppointments = "allAppointments"

    /////////////////////////////////////////////////////////////
    // Interact with Firestore db
    // https://firebase.google.com/docs/firestore/query-data/order-limit-data
    private fun dbFetchContract(contractList: MutableLiveData<List<Contract>>) {
        db.collection(allContracts)
            .get()
            .addOnSuccessListener { result ->
                Log.d(javaClass.simpleName, "allNotes fetch ${result!!.documents.size}")
                // NB: This is done on a background thread
                contractList.postValue(result.documents.mapNotNull {
                    it.toObject(Contract::class.java)
                })
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "allNotes fetch FAILED ", it)
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

}