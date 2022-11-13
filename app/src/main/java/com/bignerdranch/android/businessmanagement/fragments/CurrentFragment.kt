package com.bignerdranch.android.businessmanagement.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentCurrentBinding

class CurrentFragment : Fragment(R.layout.fragment_current) {
    companion object {
        const val titleKey = "titleKey"
        const val locationKey = "locationKey"
        const val rentKey = "rentKey"
        const val startKey = "startKey"
        const val endKey = "endKey"
        const val durationKey = "durationKey"
        const val noteKey = "noteKey"
        const val nameKey = "nameKey"
        const val phoneKey = "phoneKey"

        fun newInstance() : CurrentFragment {
            return CurrentFragment()
        }
    }

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentCurrentBinding? = null

    private val binding get() = _binding!!

//    private var resultLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                onLoopMode = result.data!!.getBooleanExtra(onLoopModeKey, onLoopMode)
//                Log.d("XXX result from setting", "$onLoopMode")
//
//                if (onLoopMode) {
//                    binding.loopButton.setBackgroundColor(resources.getColor(R.color.red, this.theme))
//                } else {
//                    binding.loopButton.setBackgroundColor(resources.getColor(R.color.transparent, this.theme))
//                }
//                player.isLooping = onLoopMode
//
//            } else {
//                Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
//            }
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrentBinding.bind(view)

        binding.currentAddBut.setOnClickListener {
            val addContractIntent = Intent(context, AddContractManager::class.java)
//            val myExtra = Bundle()
//
//            myExtra.putString(playedCountKey, playedCount)
//            myExtra.putString(onLoopModeKey, onLoopMode)
//            addContractIntent.putExtras(myExtra)
//            resultLauncher.launch(addContractIntent)
            startActivity(addContractIntent)
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}