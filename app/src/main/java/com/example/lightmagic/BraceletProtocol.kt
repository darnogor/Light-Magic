package com.example.lightmagic

import com.example.lightmagic.blinker.BlinkerProtocol
import kotlin.collections.ArrayList


class BraceletProtocol : BlinkerProtocol<Long> {

    val startFrequency: Long = 1000
    val frequency: Long = 60


    override fun bits(value: Long): List<BlinkerProtocol.Bit> {
        val delay = startFrequency + 40 * frequency
        val time  = Math.max(0, value - delay)

        val seconds          = (time / 1000).toInt()
        val secondsFirstByte = seconds shr 8
        val milliseconds     = ((time - seconds) / 4).toInt()
        val checkSum         = checksum(secondsFirstByte, seconds, milliseconds)

        val bits = ArrayList<BlinkerProtocol.Bit>()

        bits.addWakeUpBits()

        bits.addByte(secondsFirstByte)
        bits.addByte(seconds)
        bits.addByte(milliseconds)
        bits.addByte(checkSum)

        bits.addBit(0, false)

        return bits
    }


    private fun checksum(vararg bytes: Int) : Int {
        var result = 0xFF
        for (byte in bytes) {
            result = crc8(result, byte)
        }

        return result
    }


    private fun MutableList<BlinkerProtocol.Bit>.addWakeUpBits() {
        this.addBit(startFrequency, true)
    }


    private fun MutableList<BlinkerProtocol.Bit>.addByte(byte: Int) {
        this.addBit(frequency, false)

        for (index in 7 downTo 0) {
            this.addBit(frequency, (byte shr index) and 1 == 1)
        }

        this.addBit(frequency, true)
    }


    private fun MutableList<BlinkerProtocol.Bit>.addBit(frequency: Long, value: Boolean) {
        this.add(BlinkerProtocol.Bit(frequency, value))
    }


    companion object {
        private fun crc8(init: Int, byte: Int) : Int {
            var crc  = init xor byte
            val poly = 0x0D5

            for (index in 0..7) {
                if (crc and 0x80 != 0)
                    crc = (crc shl 1) xor poly
                else
                    crc = crc shl 1
            }

            crc = crc and 0xFF

            return crc
        }
    }
}