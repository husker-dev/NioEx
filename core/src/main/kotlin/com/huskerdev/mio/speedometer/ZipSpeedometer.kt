package com.huskerdev.mio.speedometer

import com.huskerdev.mio.ZipEvent
import com.huskerdev.mio.ZipEventHandler
import java.io.File
import java.util.zip.ZipEntry

class ZipSpeedometer(): Speedometer(), ZipEventHandler {

    private lateinit var lastEntry: ZipEntry
    private lateinit var lastFile: File

    val entry: ZipEntry
        get() = lastEntry

    val file: File
        get() = lastFile

    constructor(listener: (ZipSpeedometer) -> Unit): this(){
        onUpdate(listener)
    }

    fun onUpdate(listener: (ZipSpeedometer) -> Unit){
        @Suppress("UNCHECKED_CAST")
        onUpdateListeners.add(listener as (Speedometer) -> Unit)
    }

    override fun invoke(event: ZipEvent) {
        lastEntry = event.entry
        lastFile = event.file
        check(event.size, event.current)
    }
}