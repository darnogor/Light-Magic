package com.example.lightmagic

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import java.util.*


class TimePickerFragment : DialogFragment() {

    init {
        arguments = Bundle()
    }

    var listener: TimePickerDialog.OnTimeSetListener? = null
    var time: Date
        get() = arguments?.get(TIME_KEY) as Date
        set(value) = arguments?.putSerializable(TIME_KEY, value)!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.time = time

        val hour   = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(activity, listener, hour, minute, DateFormat.is24HourFormat(activity))
    }

    companion object {
        const val TIME_KEY = "time"
    }
}