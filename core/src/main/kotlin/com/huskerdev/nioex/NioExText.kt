
package com.huskerdev.nioex

import java.io.File
import java.net.URL
import java.util.zip.ZipEntry


/*
    copyTo
 */
fun File.copyTo(
    target: String,
    handler: CopyingEventHandler? = null
) = copyTo(File(target), handler)

fun Collection<File>.copyTo(
    target: Collection<String>,
    handler: CopyingEventHandler? = null
) = copyTo(target.map { File(it) }, handler)

fun Collection<File>.copyTo(
    target: Array<String>,
    handler: CopyingEventHandler? = null
) = copyTo(target.map { File(it) }, handler)

fun Array<File>.copyTo(
    target: Array<String>,
    handler: CopyingEventHandler? = null
) = copyTo(target.map { File(it) }, handler)

fun Array<File>.copyTo(
    target: Collection<String>,
    handler: CopyingEventHandler? = null
) = copyTo(target.map { File(it) }, handler)

/*
    copyToFolder
 */
fun File.copyToFolder(
    targetFolder: String,
    handler: CopyingEventHandler? = null
) = copyToFolder(File(targetFolder), handler)

fun Collection<File>.copyToFolder(
    targetFolder: String,
    handler: CopyingEventHandler? = null
) = copyToFolder(File(targetFolder), handler)

fun Array<File>.copyToFolder(
    targetFolder: String,
    handler: CopyingEventHandler? = null
) = copyToFolder(File(targetFolder), handler)

/*
    zipTo
 */
fun File.zipTo(
    target: String,
    handler: ZipEventHandler? = null
) = zipTo(File(target), handler)

fun Collection<File>.zipTo(
    target: String,
    handler: ZipEventHandler? = null
) = zipTo(File(target), handler)

fun Array<File>.zipTo(
    target: String,
    handler: ZipEventHandler? = null
) = zipTo(File(target), handler)

/*
    unzipTo
 */
fun File.unzipToFolder(
    target: String,
    handler: ZipEventHandler? = null
) = unzipToFolder(File(target), handler)

/*
    unzipFilteredTo
 */
fun File.unzipFilteredToFolder(
    target: String,
    filter: (ZipEntry) -> Boolean,
    handler: ZipEventHandler? = null
) = unzipFilteredToFolder(File(target), filter, handler)

/*
    downloadToFile
 */
fun URL.downloadToFile(
    target: String,
    handler: DownloadHandler? = null
) = downloadToFile(File(target), handler)

fun Collection<URL>.downloadToFile(
    targets: Collection<String>,
    handler: DownloadHandler? = null
) = downloadToFile(targets.map { File(it) }, handler)

fun Collection<URL>.downloadToFile(
    targets: Array<String>,
    handler: DownloadHandler? = null
) = downloadToFile(targets.map { File(it) }, handler)

fun Array<URL>.downloadToFile(
    targets: Array<String>,
    handler: DownloadHandler? = null
) = downloadToFile(targets.map { File(it) }, handler)

fun Array<URL>.downloadToFile(
    targets: Collection<String>,
    handler: DownloadHandler? = null
) = downloadToFile(targets.map { File(it) }, handler)