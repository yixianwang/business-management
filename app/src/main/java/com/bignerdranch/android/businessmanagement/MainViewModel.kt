package com.bignerdranch.android.businessmanagement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.businessmanagement.model.Contract

class MainViewModel: ViewModel() {
    private var contractList = MutableLiveData<List<Contract>>()
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
        val photoMeta = Contract(
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
        dbHelp.createPhotoMeta(photoMeta, contractList)
    }

}