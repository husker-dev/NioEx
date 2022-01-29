package com.huskerdev.nioex.speedometer

import com.huskerdev.nioex.DownloadEvent
import com.huskerdev.nioex.DownloadHandler
import java.io.File
import java.net.URL

class DownloadingSpeedometer(): Speedometer(), DownloadHandler {

    private lateinit var lastUrl: URL
    private lateinit var lastFile: File

    val file: File
        get() = lastFile

    val url: URL
        get() = lastUrl

    constructor(listener: (DownloadingSpeedometer) -> Unit): this(){
        onUpdate(listener)
    }

    fun onUpdate(listener: (DownloadingSpeedometer) -> Unit){
        @Suppress("UNCHECKED_CAST")
        onUpdateListeners.add(listener as (Speedometer) -> Unit)
    }

    override fun invoke(event: DownloadEvent) {
        lastUrl = event.url
        lastFile = event.file
        check(event.size, event.current)
    }
}