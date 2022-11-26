package com.bignerdranch.android.businessmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.bignerdranch.android.businessmanagement.model.*
import com.google.firebase.auth.FirebaseAuth
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainViewModel: ViewModel() {
    companion object {
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        var currentYear = sdf.format(Date()).split('.')[0].toInt()
        var currentMonth = sdf.format(Date()).split('.')[1].toInt()
        var currentDay = sdf.format(Date()).split('.')[2].toInt()
        var lastMonth = if (currentMonth == 1) 12 else currentMonth - 1
    }
    private var contractList = MutableLiveData<List<Contract>>()
    private var appointmentList = MutableLiveData<List<Appointment>>()
//    private var currentMonthData = MutableLiveData<Map<String, Int>>()
//    private var lastMonthData = MutableLiveData<Map<String, Int>>()
    private var accountantList = MutableLiveData<Pair<Map<String, Int>, Map<String, Int>>>()

    private val dbHelp = ViewModelDBHelper()

    // contract
    fun addNewContract(title: String,
                       location: String,
                       rent: String,
                       s_month: String,
                       s_date: String,
                       s_year: String,

                       e_month: String,
                       e_date: String,
                       e_year: String,

                       duration: String,
                       note: String,
                       name: String,
                       phone: String) {
        val contract = Contract(
            houseID = title,
            location = location,
            rent = rent,
            s_month = s_month,
            s_date = s_date,
            s_year = s_year,

            e_month = e_month,
            e_date = e_date,
            e_year = e_year,
            duration = duration,
            note = note,
            name = name,
            phone = phone
        )
        dbHelp.createContract(contract, contractList)
    }

    fun fetchContract() {
        dbHelp.fetchContract(contractList)
//        contractList.value?.sortedWith(compareBy<Contract> { it.houseID.toInt() })
//        Log.d(javaClass.simpleName, "xxxxx contractList ${contractList.value}")
    }

    fun observeContractList(): LiveData<List<Contract>> {
        return contractList
    }

    fun removeContractAt(position: Int) {
        val contract = contractList.value?.get(position)!!
        dbHelp.removeContract(contract, contractList)
    }

    // appointment
    fun addNewAppointment(title: String,
                          location: String,
                          note: String,
                          deposit: String,
                          name: String,
                          phone: String,
                          year: String,
                          month: String,
                          date: String) {
        val appointment = Appointment(
            houseID = title,
            location = location,
            deposit = deposit,
            note = note,
            name = name,
            phone = phone,
            s_year = year,
            s_month = month,
            s_date = date
        )
        dbHelp.createAppointment(appointment, appointmentList)
    }

    fun fetchAppointment() {
        dbHelp.fetchAppointment(appointmentList)
    }

    fun observeAppointmentList(): LiveData<List<Appointment>> {
        return appointmentList
    }

    fun removeAppointmentAt(position: Int) {
        val appointment = appointmentList.value?.get(position)!!
        dbHelp.removeAppointment(appointment, appointmentList)
    }

    // accountant
    fun observeAccountantList(): LiveData<Pair<Map<String, Int>, Map<String, Int>>> {
        return accountantList
    }


    private fun <T, K> Grouping<T, K>.eachSumBy(
        selector: (T) -> Int
    ): Map<K, Int> =
        fold(0) { acc, elem -> acc + selector(elem) }

    fun fetchAccountant() {
        if (contractList.value == null) {
            fetchContract()
            return
        }

        accountantList = MutableLiveData(
            Pair(
                contractList.value!!
                    .filter { it.s_month.toInt() == MainViewModel.currentMonth }
                    .groupingBy {it.houseID}
                    .eachSumBy {it.rent.toInt()},
                contractList.value!!
                    .filter { it.s_month.toInt() == MainViewModel.lastMonth }
                    .groupingBy {it.houseID}
                    .eachSumBy {it.rent.toInt()}
            )
        )
    }

    fun generatePDF(context: Context): Boolean {
        if (accountantList.value == null) {
            return false
        }

        Log.d(javaClass.simpleName, "xxx${accountantList.value}")

        val path = context.getExternalFilesDir(null)!!.absolutePath.toString() + "/summary.pdf"
        val file = File(path)

        // check file existance
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val document: Document = Document(PageSize.LETTER)

        try {
            PdfWriter.getInstance(document, FileOutputStream(file.absoluteFile))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        document.open()

        val font = Font(Font.FontFamily.COURIER, 24f)

        val paragraph: Paragraph = Paragraph()
        // edit pdf
        val keySet = HashSet<String>()
        keySet.addAll(accountantList.value!!.first.keys)
        keySet.addAll(accountantList.value!!.second.keys)

        for (i in keySet ) {
            val row = "HouseID: #${i} \n" +
                    "Cumulative Sum(Current Month): ${accountantList.value!!.first.getOrDefault(i, "0")}$ \n" +
                    "Last Month Sum: ${accountantList.value!!.second.getOrDefault(i, "0")}$ \n"
            Log.d(javaClass.simpleName, "${row}")
            paragraph.add(Paragraph(row, font))
            paragraph.add(Paragraph("\n", font))
        }
        // end edit pdf

        try {
            document.add(paragraph)
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

        document.close()
        Log.d(javaClass.simpleName, "$path")
        Toast.makeText(context, "Successfully creating summary.pdf", Toast.LENGTH_LONG).show()
        return true
    }

    // Home Fragment
    // All Houses List--local version for temp use
//    private var allHouseList = MutableLiveData<MutableList<String>>().apply {
//        value = mutableListOf()
//    }
//
//    fun insertNewHouse(id: String) {
//        val local = allHouseList.value
//        local?.let {
//            if (it.contains(id)) return@let
//            it.add(id)
//            allHouseList.value = it
//        }
//    }
//
//    fun eraseHouse(id: String) {
//        allHouseList.value?.let {
//            if (!it.contains(id)) {
//                return@let
//            }
//            it.remove(id)
//            allHouseList.value = it
//        }
//    }
//
//    fun observeAllHouseList(): LiveData<MutableList<String>> {
//        return allHouseList
//    }

    // All Houses List--database version
    private var allHouseList = MutableLiveData<List<House>>()
//        .apply { value = mutableListOf() }

    fun insertHouse(id: String) {
        val house = House(id)
        val local = allHouseList.value
        // fetchAllHousesList has to be called in MainActivity before everything
        Log.d(javaClass.simpleName, "xxx local ${local}")
        local?.let {
            if (it.contains(house)) return@let
            dbHelp.createNewHouse(house, allHouseList)
        }
    }

    fun eraseHouse(id: String) {
        val local = allHouseList.value
        local?.let {
            for (i in it.indices) {
                if (it[i].id == id) {
                    dbHelp.removeHouse(it[i], allHouseList)
                }
            }
        }
    }

    fun fetchAllHousesList() {
        dbHelp.fetchAllHousesList(allHouseList)
    }

    fun observeAllHouseList(): LiveData<List<House>> {
        return allHouseList
    }

    fun countHouse(id: String): Boolean {
        val house = House(id)
//        Log.d(javaClass.simpleName, "xxx id ${id} all list ${allHouseList.value}")
        if (allHouseList.value?.contains(house) == true) {
            return true
        }
        return false
    }

    // Table Data
    private var homeTableData = MediatorLiveData<List<HomeSummary>>().apply {
        val data = mutableListOf<HomeSummary>()

        var count = 0
        addSource(allHouseList) { list ->
            list?.let {
                count = it.size
                Log.d(javaClass.simpleName, "xxx allHouse count ${count}")
            }
        }

        var underContracts: Map<String, Int>
        addSource(contractList) { list ->
            // find all under contracts
            underContracts = list
                    .filter { it.s_year.toInt() <= currentYear }
                    .filter { currentYear <= it.e_year.toInt()}
                    .filter { it.s_month.toInt() <= currentMonth }
                    .filter { currentMonth <= it.e_month.toInt() }
                    .filter { it.s_date.toInt() <= currentDay }
                    .filter { currentDay <= it.e_date.toInt() }
                    .groupingBy { it.houseID }
                    .eachSumBy { it.duration.toInt() }
            Log.d(javaClass.simpleName, "xxx underContracts ${count} ${underContracts}")
        }

        addSource(appointmentList) { list ->
            val upcommingAppointments = list
                .filter { it.s_year.toInt() >= currentYear }
                .filter { it.s_month.toInt() >= currentMonth }
                .filter { it.s_date.toInt() > currentDay }

            Log.d(javaClass.simpleName, "xxx upcommingAppointments ${upcommingAppointments}")
        }
        value = data
    }

    fun observeHomeTableData(): LiveData<List<HomeSummary>> {
        return homeTableData
    }

    class TripleMediatorLiveData<F, S, T>(
        firstLiveData: LiveData<F>,
        secondLiveData: LiveData<S>,
        thirdLiveData: LiveData<T>
    ) : MediatorLiveData<Triple<F?, S?, T?>>() {
        init {
            addSource(firstLiveData) { firstLiveDataValue: F -> value = Triple(firstLiveDataValue, secondLiveData.value, thirdLiveData.value) }
            addSource(secondLiveData) { secondLiveDataValue: S -> value = Triple(firstLiveData.value, secondLiveDataValue, thirdLiveData.value) }
            addSource(thirdLiveData) { thirdLiveDataValue: T -> value = Triple(firstLiveData.value, secondLiveData.value, thirdLiveDataValue) }
        }
    }

    private val switchMapLiveData: LiveData<List<HomeSummary>> = TripleMediatorLiveData(
        allHouseList,
        contractList,
        appointmentList
    ).switchMap { mediatorState ->
        var count = 0
        count = mediatorState.first?.size?.toInt() ?: 0
        val houseIDList = mutableListOf<String>()
        mediatorState.first
            ?.forEach { houseIDList.add(it.id) }

        houseIDList.sort()
        Log.d(javaClass.simpleName, "xxx houseIDList ${houseIDList}")

        val underContracts = mediatorState.second?.let {
            it
                .filter { it.s_year.toInt() <= currentYear }
                .filter { currentYear <= it.e_year.toInt()}
                .filter { it.s_month.toInt() <= currentMonth }
                .filter { currentMonth <= it.e_month.toInt() }
                .filter { it.s_date.toInt() <= currentDay }
                .filter { currentDay <= it.e_date.toInt() }
//                .groupingBy { it.houseID }
//                .eachSumBy { it.duration.toInt() }
        }

        val upcommingAppointments = mediatorState.third?.let {
            it
                .filter { it.s_year.toInt() >= currentYear }
                .filter { it.s_month.toInt() >= currentMonth }
                .filter { it.s_date.toInt() > currentDay }
        }

        val data = mutableListOf<HomeSummary>()


        for (i in 1 .. count) {
            val id = houseIDList[i - 1]
            val underContractOrNot = underContracts
                ?.filter { it.houseID == id }
                ?.getOrNull(0)
            var contractFinishDate: String = ""
            if (id == underContractOrNot?.houseID) {
                contractFinishDate += underContractOrNot.e_month + "/"
                contractFinishDate += underContractOrNot.e_date + "/"
                contractFinishDate += underContractOrNot.e_year
            }


            upcommingAppointments
                ?.filter { it.houseID == id }
                ?.toMutableList()
                ?.sortWith(compareBy<Appointment> { it.s_year.toInt() }.thenBy { it.s_month.toInt() }.thenBy { it.s_date.toInt() })

            var firstUpCommingApp = upcommingAppointments?.get(0)
            var temp_y: Int = 0x3f3f3f3f
            var temp_m: Int = 0x3f3f3f3f
            var temp_d: Int = 0x3f3f3f3f
            upcommingAppointments
                ?.forEach {
                    if (it.houseID == id
                        && it.s_year.toInt() < temp_y
                        && it.s_month.toInt() < temp_m
                        && it.s_date.toInt() < temp_d) {
                        firstUpCommingApp = it
                        temp_y = it.s_year.toInt()
                        temp_m = it.s_month.toInt()
                        temp_d = it.s_date.toInt()
                    }
                }
            Log.d(javaClass.simpleName, "xxx firstUpCommingApp ${firstUpCommingApp}")



            var upComingDate: String = ""
            if (id == firstUpCommingApp?.houseID) {
                upComingDate += firstUpCommingApp?.s_month + "/"
                upComingDate += firstUpCommingApp?.s_date + "/"
                upComingDate += firstUpCommingApp?.s_year
            }

            data.add(HomeSummary(id, contractFinishDate, upComingDate))
        }

        return@switchMap liveData {
            emit(data)
        }
    }

    fun observeSwitchMapLiveData(): LiveData<List<HomeSummary>> {
        return switchMapLiveData
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}

















