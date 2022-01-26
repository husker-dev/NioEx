package com.huskerdev.mio

import java.io.File
import java.net.URL
import java.util.zip.ZipEntry

typealias CopyingEventHandler = (CopyingEvent) -> Unit
typealias ZipEventHandler = (ZipEvent) -> Unit
typealias DownloadHandler = (DownloadEvent) -> Unit

data class CopyingEvent(
    val size: Long,
    val current: Long,
    val source: File,
    val target: File
)

data class ZipEvent(
    val size: Long,
    val current: Long,
    val entry: ZipEntry,
    val file: File
)

data class DownloadEvent(
    val size: Long,
    val current: Long,
    val url: URL,
    val file: File
)