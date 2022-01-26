package com.huskerdev.mio

import com.huskerdev.mio.processor.impl.LinuxIO
import com.huskerdev.mio.processor.impl.MacIO
import com.huskerdev.mio.processor.impl.UniversalIO
import com.huskerdev.mio.processor.impl.WinIO
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.util.zip.ZipEntry
import kotlin.streams.toList


private val processor by lazy {
    val os = System.getProperty("os.name").lowercase()
    return@lazy when {
        "mac" in os || "darwin" in os -> MacIO()
        "win" in os -> WinIO()
        "nux" in os -> LinuxIO()
        else -> UniversalIO()
    }
}

val File.treeElementsReversed: List<File>
    get() = Files.walk(toPath())
                .sorted(Comparator.reverseOrder())
                .map { File(it.toFile().absolutePath) }
                .toList()

val File.treeElements: List<File>
    get() = Files.walk(toPath())
                .map { File(it.toFile().absolutePath) }
                .toList()

/*
    walkTreeReversed
 */
fun File.walkTreeReversed(handler: (File) -> Unit) {
    Files.walk(toPath())
        .sorted(Comparator.reverseOrder())
        .forEach { handler(File(it.toFile().absolutePath)) }
}

/*
    walkTree
 */
fun File.walkTree(handler: (File) -> Unit) {
    Files.walk(toPath())
        .forEach { handler(File(it.toFile().absolutePath)) }
}

/*
    copyTo
 */
fun File.copyTo(
    target: File,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(arrayOf(this), arrayOf(target), handler)

fun Collection<File>.copyTo(
    target: Collection<File>,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(toTypedArray(), target.toTypedArray(), handler)

fun Collection<File>.copyTo(
    target: Array<File>,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(toTypedArray(), target, handler)

fun Array<File>.copyTo(
    target: Array<File>,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(this, target, handler)

fun Array<File>.copyTo(
    target: Collection<File>,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(this, target.toTypedArray(), handler)

/*
    copyToFolder
 */
fun File.copyToFolder(
    targetFolder: File,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(arrayOf(this), arrayOf(File(targetFolder, name)), handler)

fun Collection<File>.copyToFolder(
    targetFolder: File,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(this.toTypedArray(), map { File(targetFolder, it.name) }.toTypedArray(), handler)

fun Array<File>.copyToFolder(
    targetFolder: File,
    handler: CopyingEventHandler? = null
) = processor.copyFilesToFiles(this, map { File(targetFolder, it.name) }.toTypedArray(), handler)

/*
    zipTo
 */
fun File.zipTo(
    target: File,
    handler: ZipEventHandler? = null
) = processor.zipFiles(arrayOf(this), target, handler)

fun Collection<File>.zipTo(
    target: File,
    handler: ZipEventHandler? = null
) = processor.zipFiles(toTypedArray(), target, handler)

fun Array<File>.zipTo(
    target: File,
    handler: ZipEventHandler? = null
) = processor.zipFiles(this, target, handler)

/*
    unzipTo
 */
fun File.unzipTo(
    target: File,
    handler: ZipEventHandler? = null
) = processor.unzipFile(this, target, { true }, handler)

/*
    unzipFilteredTo
 */
fun File.unzipFilteredTo(
    target: File,
    filter: (ZipEntry) -> Boolean,
    handler: ZipEventHandler? = null
) = processor.unzipFile(this, target, filter, handler)

/*
    walkZipEntries
 */
fun File.walkZipEntries(handler: (ZipEntry) -> Unit) = processor.walkZipEntries(this, handler)

/*
    zipEntries
 */
fun File.zipEntries() = processor.zipEntries(this)

/*
    downloadToFile
 */
fun URL.downloadToFile(
    target: File,
    handler: DownloadHandler? = null
) = processor.downloadToFiles(arrayOf(this), arrayOf(target), handler)

fun Collection<URL>.downloadToFile(
    targets: Collection<File>,
    handler: DownloadHandler? = null
) = processor.downloadToFiles(this.toTypedArray(), targets.toTypedArray(), handler)

fun Collection<URL>.downloadToFile(
    targets: Array<File>,
    handler: DownloadHandler? = null
) = processor.downloadToFiles(this.toTypedArray(), targets, handler)

fun Array<URL>.downloadToFile(
    targets: Array<File>,
    handler: DownloadHandler? = null
) = processor.downloadToFiles(this, targets, handler)

fun Array<URL>.downloadToFile(
    targets: Collection<File>,
    handler: DownloadHandler? = null
) = processor.downloadToFiles(this, targets.toTypedArray(), handler)


val File.size: Long
    get() = processor.getFileSize(this)

var File.creationTime: Long
    get() = processor.getFileCreationTime(this)
    set(value) = processor.setFileCreationTime(this, value)

var File.lastAccessTime: Long
    get() = processor.getFileLastAccessTime(this)
    set(value) = processor.setFileLastAccessTime(this, value)

var File.lastModifiedTime: Long
    get() = processor.getFileLastModifiedTime(this)
    set(value) = processor.setFileLastModifiedTime(this, value)

val File.children: List<File>
    get() = processor.getChildren(this)