package com.example.notificationtimerapp

import android.app.AlarmManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()
    companion object {
        fun newInstance() = TimerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        val textview = view.findViewById<TextView>(R.id.timer)
        val timeObserver = Observer<Long> {time -> textview.text = time.toString()}
        viewModel.elapsedTime.observe(viewLifecycleOwner, timeObserver)


        val startButton = view.findViewById<Button>(R.id.start_button)
        val resetButton = view.findViewById<Button>(R.id.reset_button)



        startButton.setOnClickListener { viewModel.setAlarm(true) }
        resetButton.setOnClickListener { viewModel.setAlarm(false) }

        return view
    }

}
