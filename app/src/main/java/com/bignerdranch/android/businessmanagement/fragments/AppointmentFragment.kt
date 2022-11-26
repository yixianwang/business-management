package com.bignerdranch.android.businessmanagement.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.businessmanagement.MainViewModel
import com.bignerdranch.android.businessmanagement.R
import com.bignerdranch.android.businessmanagement.databinding.FragmentAppointmentBinding
import com.bignerdranch.android.businessmanagement.fragments.adapters.AppointmentAdapter
import com.bignerdranch.android.businessmanagement.fragments.managers.AddAppointmentManager
import com.bignerdranch.android.businessmanagement.model.Appointment
import com.bignerdranch.android.businessmanagement.model.Contract

class AppointmentFragment : Fragment(R.layout.fragment_appointment) {
    companion object {
        fun newInstance() : AppointmentFragment {
            return AppointmentFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FragmentAppointmentBinding? = null

    private val binding get() = _binding!!

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.fetchAppointment()
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
                    viewModel.removeAppointmentAt(position)
                }
            }
        return ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAppointmentBinding.bind(view)

        val adapter = AppointmentAdapter(viewModel)
        val rv = binding.appointmentRv
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)
        initTouchHelper().attachToRecyclerView(rv)

        viewModel.observeAppointmentList().observe(viewLifecycleOwner) {
            val sortedAppointmentList = it
                .sortedWith(compareBy<Appointment> { it.houseID.toInt() }
                    .thenBy { it.s_year.toInt() }
                    .thenBy { it.s_month.toInt() }
                    .thenBy { it.s_date.toInt() })

            adapter.submitList(sortedAppointmentList)
        }


        binding.appointmentAddBut.setOnClickListener {
            val addAppointmentIntent = Intent(context, AddAppointmentManager::class.java)
            val myExtra = Bundle()
//            myExtra.putString(playedCountKey, playedCount)
//            myExtra.putString(onLoopModeKey, onLoopMode)
            addAppointmentIntent.putExtras(myExtra)
            resultLauncher.launch(addAppointmentIntent)
//            startActivity(addAppointmentIntent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.fetchAppointment()
            } else {
                Log.w(javaClass.simpleName, "Bad activity return code ${result.resultCode}")
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

//    override fun onDestroyView() {
//        _binding = null
//        super.onDestroyView()
//    }
}