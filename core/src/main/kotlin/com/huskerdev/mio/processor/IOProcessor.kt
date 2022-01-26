package com.huskerdev.mio.processor

import com.huskerdev.mio.*
import com.huskerdev.mio.speedometer.Speedometer
import java.io.File
import java.net.URL
import java.util.zip.ZipEntry

abstract class IOProcessor {

    abstract fun getChildren(file: File): List<File>

    abstract fun getFileSize(file: File): Long

    abstract fun getFileCreationTime(file: File): Long
    abstract fun setFileCreationTime(file: File, value: Long)

    abstract fun getFileLastAccessTime(file: File): Long
    abstract fun setFileLastAccessTime(file: File, value: Long)

    abstract fun getFileLastModifiedTime(file: File): Long
    abstract fun setFileLastModifiedTime(file: File, value: Long)

    abstract fun copyFilesToFiles(sources: Array<File>, targets: Array<File>, handler: CopyingEventHandler?): Array<File>

    abstract fun walkZipEntries(file: File, handler: (ZipEntry) -> Unit)
    abstract fun zipEntries(file: File): List<ZipEntry>

    abstract fun zipFiles(files: Array<File>, target: File, handler: ZipEventHandler?): File
    abstract fun unzipFile(file: File, target: File, filter: (ZipEntry) -> Boolean, handler: ZipEventHandler?)

    abstract fun downloadToFiles(urls: Array<URL>, targets: Array<File>, handler: DownloadHandler?)

    protected fun <T> assumeSpeedometer(handler: Any?, run: () -> T): T{
        if(handler is Speedometer)
            handler.started()
        val result = run()
        if(handler is Speedometer)
            handler.completed()
        return result
    }
}