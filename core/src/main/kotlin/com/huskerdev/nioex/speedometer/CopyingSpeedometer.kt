package com.huskerdev.nioex.speedometer

import com.huskerdev.nioex.CopyingEvent
import com.huskerdev.nioex.CopyingEventHandler
import java.io.File

class CopyingSpeedometer(): Speedometer(), CopyingEventHandler {

    private lateinit var lastSource: File
    private lateinit var lastTarget: File

    val source: File
        get() = lastSource

    val target: File
        get() = lastTarget

    constructor(listener: (CopyingSpeedometer) -> Unit): this(){
        onUpdate(listener)
    }

    fun onUpdate(listener: (CopyingSpeedometer) -> Unit){
        @Suppress("UNCHECKED_CAST")
        onUpdateListeners.add(listener as (Speedometer) -> Unit)
    }

    override fun invoke(event: CopyingEvent) {
        lastSource = event.source
        lastTarget = event.target
        check(event.size, event.current)
    }
}