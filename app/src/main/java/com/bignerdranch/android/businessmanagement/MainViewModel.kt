package com.bignerdranch.android.businessmanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.businessmanagement.model.Contract

class MainViewModel: ViewModel() {
    private var contractList = MutableLiveData<List<Contract>>()

    fun addNewContract(title: String,
                        location: String,
                        rent: String,
                        start: String,
                        end: String,
                        duration: String,
                        note: String,
                        name: String,
                        phone: String) {
        contractList.
    }

}