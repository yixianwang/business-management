package com.bignerdranch.android.businessmanagement.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AddContractManager : AppCompatActivity() {
    private var resultLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onLoopMode = result.data!!.getBooleanExtra(onLoopModeKey, onLoopMode)
                Log.d("XXX result from setting", "$onLoopMode")

                if (onLoopMode) {
                    binding.loopButton.setBackgroundColor(resources.getColor(R.color.red, this.theme))
                } else {
                    binding.loopButton.setBackgroundColor(resources.getColor(R.color.transparent, this.theme))
                }
                player.isLooping = onLoopMode

            } else {
                Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}