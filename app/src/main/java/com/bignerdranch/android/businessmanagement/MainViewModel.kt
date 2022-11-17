package com.bignerdranch.android.businessmanagement

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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class MainViewModel: ViewModel() {
    private var contractList = MutableLiveData<List<Contract>>()
    private var appointmentList = MutableLiveData<List<Appointment>>()
//    private var currentMonthData = MutableLiveData<Map<String, Int>>()
//    private var lastMonthData = MutableLiveData<Map<String, Int>>()
    private var accountantList = MutableLiveData<Pair<Map<String, Int>, Map<String, Int>>>()

    private val dbHelp = ViewModelDBHelper()

    private val sdf = SimpleDateFormat("yyyy.MM.dd")
    private val currentMonth = sdf.format(Date()).split('.')[1].toInt()
    private val lastMonth = if (currentMonth == 1) 12 else currentMonth - 1

    // contract
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
//        currentMonthData.value = contractList.value!!
//            .filter { it.start.split('/')[0].toInt() == currentMonth }
//            .groupingBy {it.title}
//            .eachSumBy {it.rent.toInt()}
//
//        lastMonthData.value = contractList.value!!
//            .filter { it.start.split('/')[0].toInt() == lastMonth }
//            .groupingBy {it.title}
//            .eachSumBy {it.rent.toInt()}

//        currentMonthData.value = contractList.value!!
//            .filter { it.start.split('/')[0].toInt() == currentMonth || it.start.split('/')[0].toInt() == lastMonth }
//            .groupingBy {Key(it.title, it.start.split('/')[0].toInt())}
//            .eachSumBy {it.rent.toInt()}

        accountantList = MutableLiveData(
            Pair(
                contractList.value!!
                    .filter { it.start.split('/')[0].toInt() == currentMonth }
                    .groupingBy {it.title}
                    .eachSumBy {it.rent.toInt()},
                contractList.value!!
                    .filter { it.start.split('/')[0].toInt() == lastMonth }
                    .groupingBy {it.title}
                    .eachSumBy {it.rent.toInt()}
            )
        )
    }

    fun generatePDF(context: Context) {
        val path = context.getExternalFilesDir(null)!!.absolutePath.toString() + "/users.pdf"
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
        paragraph.add(Paragraph("111", font))
        paragraph.add(Paragraph("111", font))
        paragraph.add(Paragraph("\n", font))
        paragraph.add(Paragraph("111", font))
        paragraph.add(Paragraph("111", font))

        try {
            document.add(paragraph)
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

        document.close()
        Log.d(javaClass.simpleName, "$path")
        Toast.makeText(context, "Successfully creating PDF", Toast.LENGTH_LONG).show()
    }

}

















