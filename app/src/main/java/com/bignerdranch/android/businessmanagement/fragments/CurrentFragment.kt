package com.bignerdranch.android.businessmanagement.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentCurrentBinding
import com.bignerdranch.android.businessmanagement.fragments.adapters.CurrentAdapter
import com.bignerdranch.android.businessmanagement.fragments.managers.AddContractManager

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
//                val title = result.data!!.getStringExtra(titleKey)
//                val location = result.data!!.getStringExtra(locationKey)
//                val rent = result.data!!.getStringExtra(rentKey)
//                val start = result.data!!.getStringExtra(startKey)
//                val end = result.data!!.getStringExtra(endKey)
//                val duration = result.data!!.getStringExtra(durationKey)
//                val note = result.data!!.getStringExtra(noteKey)
//                val name = result.data!!.getStringExtra(nameKey)
//                val phone = result.data!!.getStringExtra(phoneKey)
//
//                viewModel.addNewContract(title!!,
//                    location!!,
//                    rent!!,
//                    start!!,
//                    end!!,
//                    duration!!,
//                    note!!,
//                    name!!,
//                    phone!!)

                viewModel.fetchContract()

            } else {
                Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
            }
        }

    private fun getPos(holder: RecyclerView.ViewHolder) : Int {
        val pos = holder.bindingAdapterPosition
        // notifyDataSetChanged was called, so position is not known
        if( pos == RecyclerView.NO_POSITION) {
            return holder.absoluteAdapterPosition
        }
        return pos
    }

    // Touch helpers provide functionality like detecting swipes or moving
    // entries in a recycler view.  Here we do swipe left to delete
    private fun initTouchHelper(): ItemTouchHelper {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START)
            {
                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                      direction: Int) {
                    val position = getPos(viewHolder)
                    Log.d(javaClass.simpleName, "Swipe delete $position")
                    viewModel.removeContractAt(position)
                }
            }
        return ItemTouchHelper(simpleItemTouchCallback)
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
        initTouchHelper().attachToRecyclerView(rv)

        viewModel.observeContractList().observe(viewLifecycleOwner) {
            Log.d(javaClass.simpleName, "${it}")
            adapter.submitList(it)
        }


        binding.currentAddBut.setOnClickListener {
            val addContractIntent = Intent(context, AddContractManager::class.java)
            val myExtra = Bundle()
//            myExtra.putString(playedCountKey, playedCount)
//            myExtra.putString(onLoopModeKey, onLoopMode)
            addContractIntent.putExtras(myExtra)
            resultLauncher.launch(addContractIntent)
//            startActivity(addContractIntent)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}