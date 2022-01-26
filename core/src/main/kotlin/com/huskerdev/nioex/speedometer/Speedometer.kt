package com.huskerdev.nioex.speedometer

import kotlin.math.pow
import kotlin.math.roundToInt


abstract class Speedometer {

    private var lastSize = 0L
    private var lastCurrent = 0L
    private var speed = 0.0

    protected var onUpdateListeners = arrayListOf<(Speedometer) -> Unit>()
    protected var onStartListeners = arrayListOf<() -> Unit>()
    protected var onCompleteListeners = arrayListOf<() -> Unit>()

    val bytesPerSecond: Double
        get() = speed
    val percent: Double
        get() = lastCurrent.toDouble() / lastSize
    val size: Long
        get() = lastSize
    val current: Long
        get() = lastCurrent

    var updateDelay = 10
    private var lastUpdated = 0L

    var speedUpdateDelay = 1000
    private var lastSpeedUpdated = 0L

    open fun reset(){
        lastUpdated = 0L
        lastSpeedUpdated = 0L
        speed = 0.0
    }

    open fun check(size: Long, current: Long){
        val currentTime = System.currentTimeMillis()

        if(currentTime - lastUpdated > updateDelay){
            lastUpdated = currentTime
            speed = (currentTime - lastCurrent) * (1000.0 / updateDelay)
        }
        if(currentTime - lastSpeedUpdated > speedUpdateDelay){
            lastSpeedUpdated = currentTime

            lastSize = size
            lastCurrent = current
            onUpdateListeners.forEach { it(this) }
        }
    }

    open fun onStarted(listener: () -> Unit){
        onStartListeners.add(listener)
    }

    open fun onCompleted(listener: () -> Unit){
        onCompleteListeners.add(listener)
    }

    open fun started(){
        reset()
        onStartListeners.forEach { it() }
    }

    open fun completed(){
        onCompleteListeners.forEach { it() }
    }

    open val formattedSpeedString: String
        get() {
            val kb = (bytesPerSecond / 1024).roundTo(2)
            val mb = (bytesPerSecond / 1024 / 1024).roundTo(2)

            return if(mb >= 1) "$mb MB/sec"
            else if(kb >= 1) "$kb KB/sec"
            else "$bytesPerSecond B/sec"
        }

    private fun Double.roundTo(digits: Int): Double {
        val factor = 10.0.pow(digits.toDouble())
        return (this * factor).roundToInt() / factor
    }
}