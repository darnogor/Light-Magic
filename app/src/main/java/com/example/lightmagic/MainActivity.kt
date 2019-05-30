package com.example.lightmagic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.lightmagic.blinker.Blinker
import com.example.lightmagic.blinker.Torch
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var torch: Torch
    private lateinit var blinker: Blinker<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        torch   = CameraTorch(this)
        blinker = BraceletBlinker(torch)

        setupButton.setOnClickListener { blinker.blink(13 * 60 * 1000) }
    }
}
