package com.bignerdranch.android.businessmanagement

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.businessmanagement.model.Accountant
import com.bignerdranch.android.businessmanagement.model.Appointment
import com.bignerdranch.android.businessmanagement.model.Contract
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
            title = title,
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
                          startDate: String) {
        val appointment = Appointment(
            title = title,
            location = location,
            deposit = deposit,
            note = note,
            name = name,
            phone = phone,
            startDate = startDate
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
                    .groupingBy {it.title}
                    .eachSumBy {it.rent.toInt()},
                contractList.value!!
                    .filter { it.s_month.toInt() == MainViewModel.lastMonth }
                    .groupingBy {it.title}
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


}

















