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
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.businessmanagement.MainViewModel
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

    private val viewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentCurrentBinding? = null

    private val binding get() = _binding!!

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val t = result.data!!

                val title = result.data!!.getStringExtra(titleKey)
                val location = result.data!!.getStringExtra(locationKey)
                val rent = result.data!!.getStringExtra(rentKey)
                val start = result.data!!.getStringExtra(startKey)
                val end = result.data!!.getStringExtra(endKey)
                val duration = result.data!!.getStringExtra(durationKey)
                val note = result.data!!.getStringExtra(noteKey)
                val name = result.data!!.getStringExtra(nameKey)
                val phone = result.data!!.getStringExtra(phoneKey)

                viewModel.addNewContract(title!!,
                    location!!,
                    rent!!,
                    start!!,
                    end!!,
                    duration!!,
                    note!!,
                    name!!,
                    phone!!)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrentBinding.bind(view)

        val adapter = CurrentAdapter(viewModel)
        val rv = binding.currentRv
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)

        viewModel.observeContractList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "xxx${it}")
            adapter.submitList(it)
        }


        binding.currentAddBut.setOnClickListener {
            val addContractIntent = Intent(context, AddContractManager::class.java)
//            val myExtra = Bundle()
//
//            myExtra.putString(playedCountKey, playedCount)
//            myExtra.putString(onLoopModeKey, onLoopMode)
//            addContractIntent.putExtras(myExtra)
//            resultLauncher.launch(addContractIntent)
            startActivity(addContractIntent)
            viewModel.fetchContract()
        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}