package com.example.lightmagic.blinker

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


open class Blinker<T>(var torch: Torch, var protocol: BlinkerProtocol<T>) {
    private var executorService : ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private var completeFuture : ScheduledFuture<*>? = null


    fun blink(value : T) {
        if (completeFuture == null || completeFuture!!.isDone) {
            blink(protocol.bits(value))
        }
    }


    private fun blink(bits: List<BlinkerProtocol.Bit>) {
        var delay: Long = 0

        for (bit in bits) {
            completeFuture = executorService.schedule({
                if (bit.value)
                    torch.on()
                else
                    torch.off()
            }, delay, TimeUnit.MILLISECONDS)

            delay += bit.frequency
        }
    }
}