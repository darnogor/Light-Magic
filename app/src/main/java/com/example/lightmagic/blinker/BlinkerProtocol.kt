package com.example.lightmagic.blinker


interface BlinkerProtocol<T> {
    data class Bit(var frequency: Long, var value: Boolean)

    fun bits(value : T) : List<Bit>
}