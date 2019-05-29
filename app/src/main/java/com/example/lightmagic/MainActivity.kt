package com.example.lightmagic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var torch: Torch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        torchSwitch.setOnCheckedChangeListener { _, isChecked -> torch.lightOn = isChecked }

        torch = Torch(this)
    }
}
