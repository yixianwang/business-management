package com.bignerdranch.android.businessmanagement

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.businessmanagement.model.Contract
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ViewModelDBHelper() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val allContracts = "allContracts"

    /////////////////////////////////////////////////////////////
    // Interact with Firestore db
    // https://firebase.google.com/docs/firestore/query-data/order-limit-data
    private fun dbFetchPhotoMeta(contractList: MutableLiveData<List<Contract>>) {
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

    // https://firebase.google.com/docs/firestore/manage-data/add-data#add_a_document
    fun createPhotoMeta(
        contract: Contract,
        contractList: MutableLiveData<List<Contract>>
    ) {
        contract.firestoreID = db.collection(allContracts).document().id

        db.collection(allContracts)
            .add(contract)
            .addOnSuccessListener {
                dbFetchPhotoMeta(contractList)
            }
            .addOnFailureListener {
                Log.d(javaClass.simpleName, "fail create")
            }
    }

}