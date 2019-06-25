package com.example.lightmagic

import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.TimePicker
import com.example.lightmagic.blinker.Blinker
import com.example.lightmagic.blinker.Torch
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var torch: Torch
    private lateinit var blinker: Blinker<Long>

    private var time: Date = Date()


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(TIME_KEY, time)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val savedTime = savedInstanceState?.get(TIME_KEY) as? Date
        if (savedTime != null) {
            time = savedTime
        }

        torch   = CameraTorch(this)
        blinker = Blinker(torch, BraceletProtocol())

        initTimeView()
        initSetupButton()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        this.setTime(calendar.time)
    }

    private fun initSetupButton() {
        setupButton.setOnClickListener {
            val millisecondsLeft = Math.max(0, time.time - Date().time)
            blinker.blink(millisecondsLeft)
        }
    }


    private fun initTimeView() {
        timeView.setOnClickListener { showTimePicker() }
        this.setTime(time)
    }


    private fun showTimePicker() {
        val timePicker = TimePickerFragment()
        timePicker.listener = this
        timePicker.time = time
        timePicker.show(supportFragmentManager, TIME_PICKER_DIALOG_TAG)
    }


    private fun setTime(time: Date) {
        this.time = time

        timeView.setTime(time)
        setupButton.isEnabled = time > Date()
    }


    private fun TextView.setTime(time: Date) {
        this.text = DateFormat.format("hh:mm", time)
    }


    companion object {
        const val TIME_KEY = "time"
        const val TIME_PICKER_DIALOG_TAG = "timePicker"
    }
}
