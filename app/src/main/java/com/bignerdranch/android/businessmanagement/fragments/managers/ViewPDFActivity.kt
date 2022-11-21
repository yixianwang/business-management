package com.bignerdranch.android.businessmanagement.fragments.managers

import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.businessmanagement.databinding.ActivityViewPdfactivityBinding
import java.io.File

class ViewPDFActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityViewPdfactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val path = this.getExternalFilesDir(null)!!.absolutePath.toString() + "/summary.pdf"
        Log.d(javaClass.simpleName, "${path}")
        val file = File(path)
        binding.pdfViewer
            .fromFile(file)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .enableAnnotationRendering(true)
            .defaultPage(0)
            .scrollHandle(null)
            .password(null)
            .load()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.home -> finish()
        }
        return true
    }

}