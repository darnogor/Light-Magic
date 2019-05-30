package com.example.lightmagic

import com.example.lightmagic.blinker.Blinker
import com.example.lightmagic.blinker.BlinkerProtocol
import com.example.lightmagic.blinker.Torch
import kotlin.collections.ArrayList


class BraceletBlinker(torch: Torch) : Blinker<Long>(torch, BraceletProtocol()) {

    private class BraceletProtocol : BlinkerProtocol<Long> {

        val startFrequency: Long = 1000
        val frequency: Long = 30


        override fun bits(value: Long): List<BlinkerProtocol.Bit> {
            val seconds          = (value / 1000).toInt()
            val secondsFirstByte = seconds shr 8
            val milliseconds     = ((value - seconds) / 4).toInt()
            val checkSum         = secondsFirstByte and seconds and milliseconds

            val bits = ArrayList<BlinkerProtocol.Bit>()

            bits.addWakeUpBits()

            bits.addByte(secondsFirstByte)
            bits.addByte(seconds)
            bits.addByte(milliseconds)

            bits.addByte(checkSum)

            return bits
        }


        private fun MutableList<BlinkerProtocol.Bit>.addWakeUpBits() {
            this.addBit(startFrequency, true)
            this.addBit(frequency, false)
        }


        private fun MutableList<BlinkerProtocol.Bit>.addByte(byte: Int) {
            this.addBit(frequency, true)

            for (index in 7 downTo 0) {
                this.addBit(frequency, (byte shr index) and 1 == 1)
            }

            this.addBit(frequency, true)
            this.addBit(frequency, false)
        }


        private fun MutableList<BlinkerProtocol.Bit>.addBit(frequency: Long, value: Boolean) {
            this.add(BlinkerProtocol.Bit(frequency, value))
        }
    }
}