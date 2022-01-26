package com.huskerdev.nioex.processor.impl

import com.huskerdev.nioex.*
import com.huskerdev.nioex.processor.IOProcessor
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.nio.file.Files
import java.nio.file.attribute.FileTime
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


open class UniversalIO: IOProcessor() {

    private val bufferSize = 1024 * 8   // 8 Mb

    override fun getChildren(file: File): List<File> {
        return file.listFiles()?.map { File(it.absolutePath) }?.toList() ?: emptyList()
    }

    override fun getFileSize(file: File): Long {
        return if(file.isDirectory) {
            var size = 0L
            file.walkTree { size += it.length() }
            size
        }else file.length()
    }

    override fun getFileCreationTime(file: File): Long {
        return (Files.getAttribute(file.toPath(), "creationTime") as FileTime).toMillis()
    }

    override fun setFileCreationTime(file: File, value: Long) {
        Files.setAttribute(file.toPath(), "creationTime", FileTime.fromMillis(value))
    }

    override fun getFileLastAccessTime(file: File): Long {
        return (Files.getAttribute(file.toPath(), "lastAccessTime") as FileTime).toMillis()
    }

    override fun setFileLastAccessTime(file: File, value: Long) {
        Files.setAttribute(file.toPath(), "lastAccessTime", FileTime.fromMillis(value))
    }

    override fun getFileLastModifiedTime(file: File): Long {
        return (Files.getAttribute(file.toPath(), "lastModifiedTime") as FileTime).toMillis()
    }

    override fun setFileLastModifiedTime(file: File, value: Long) {
        Files.setAttribute(file.toPath(), "lastModifiedTime", FileTime.fromMillis(value))
    }

    override fun copyFilesToFiles(
        sources: Array<File>,
        targets: Array<File>,
        handler: CopyingEventHandler?
    ): Array<File> = assumeSpeedometer(handler) {
        val size = sources.sumOf { it.size }
        var copied = 0L

        for(i in sources.indices){
            val source = sources[i]
            val target = targets[i]

            if(source.isDirectory){
                target.mkdirs()

                source.walkTree {
                    val newFile = File(target.absoluteFile, it.toRelativeString(source.absoluteFile))
                    if(!it.isDirectory) {
                        val input = FileInputStream(it)
                        val output = FileOutputStream(newFile)
                        copyStreams(input, output) { copiedBytes ->
                            copied += copiedBytes
                            handler?.invoke(CopyingEvent(size, copied, it, newFile))
                        }
                        output.close()
                        input.close()
                    }else newFile.mkdirs()

                    if(!it.isDirectory || it.list()!!.isEmpty()) {
                        newFile.lastAccessTime = it.lastAccessTime
                        newFile.lastModifiedTime = it.lastModifiedTime
                        newFile.creationTime = it.creationTime
                    }
                }
            }else{
                val input = FileInputStream(source)
                val output = FileOutputStream(target)
                copyStreams(input, output) { copiedBytes ->
                    copied += copiedBytes
                    handler?.invoke(CopyingEvent(size, copied, source, target))
                }
                output.close()
                input.close()
            }
        }

        return@assumeSpeedometer targets
    }

    override fun walkZipEntries(file: File, handler: (ZipEntry) -> Unit) {
        val input = ZipInputStream(FileInputStream(file))

        var entry: ZipEntry?
        while(input.nextEntry.also { entry = it } != null){
            handler.invoke(entry!!)
            input.closeEntry()
        }
        input.close()
    }

    override fun zipEntries(file: File): List<ZipEntry> {
        val entries = arrayListOf<ZipEntry>()
        walkZipEntries(file) { entries.add(it) }
        return entries
    }

    override fun zipFiles(
        files: Array<File>,
        target: File,
        handler: ZipEventHandler?
    ): File = assumeSpeedometer(handler) {
        val output = ZipOutputStream(FileOutputStream(target))

        val size = files.sumOf { it.size }
        var zipped = 0L

        for(file in files){
            file.walkTree {
                val entry = ZipEntry(it.absoluteFile.relativeTo(file.absoluteFile.parentFile).path + if(it.isDirectory) "/" else "")
                output.putNextEntry(entry)

                if(!it.isDirectory){
                    val input = FileInputStream(it)
                    copyStreams(input, output) { zippedBytes ->
                        zipped += zippedBytes
                        handler?.invoke(ZipEvent(size, zipped, entry, it.absoluteFile))
                    }
                    input.close()
                }
                entry.lastAccessTime = FileTime.fromMillis(it.lastAccessTime)
                entry.lastModifiedTime = FileTime.fromMillis(it.lastModifiedTime)
                entry.creationTime = FileTime.fromMillis(it.creationTime)

                output.closeEntry()
            }
        }
        output.close()

        return@assumeSpeedometer target.absoluteFile
    }

    override fun unzipFile(
        file: File,
        target: File,
        filter: (ZipEntry) -> Boolean,
        handler: ZipEventHandler?
    ) = assumeSpeedometer(handler) {
        val input = ZipInputStream(FileInputStream(file))

        var size = 0L
        var unzipped = 0L
        walkZipEntries(file) { size += it.size }

        var entry: ZipEntry?
        while(input.nextEntry.also { entry = it } != null){
            if(filter(entry!!)){
                val newFile = File(target.absoluteFile, entry!!.name)

                if(!entry!!.isDirectory){
                    newFile.parentFile.mkdirs()

                    val output = FileOutputStream(newFile)
                    copyStreams(input, output){ unzippedBytes ->
                        unzipped += unzippedBytes
                        handler?.invoke(ZipEvent(size, unzipped, entry!!, newFile))
                    }
                    output.close()
                }else newFile.mkdirs()

                if(entry!!.lastAccessTime != null)
                    newFile.lastAccessTime = entry!!.lastAccessTime.toMillis()
                if(entry!!.lastModifiedTime != null)
                    newFile.lastModifiedTime = entry!!.lastModifiedTime.toMillis()
                if(entry!!.creationTime != null)
                    newFile.creationTime = entry!!.creationTime.toMillis()
            }
            input.closeEntry()
        }
        input.close()
    }

    override fun downloadToFiles(
        urls: Array<URL>,
        targets: Array<File>,
        handler: DownloadHandler?
    ) = assumeSpeedometer(handler) {
        val openedConnections = hashMapOf<URL, URLConnection>()

        val size = urls.sumOf {
            val connection = it.openConnection()
            openedConnections[it] = connection

            connection.getInputStream()
            connection.contentLengthLong
        }
        var downloaded = 0L

        for(i in urls.indices){
            val url = urls[i]
            val target = targets[i]
            val connection = openedConnections[url]

            target.absoluteFile.parentFile.mkdirs()
            val input = connection!!.getInputStream()!!
            val output = FileOutputStream(target)
            copyStreams(input, output){ downloadedBytes ->
                downloaded += downloadedBytes
                handler?.invoke(DownloadEvent(size, downloaded, url, target))
            }
            input.close()
            output.close()
        }
    }

    private fun copyStreams(input: InputStream, output: OutputStream, completed: (Int) -> Unit){
        val buffer = ByteArray(bufferSize)
        var lengthRead: Int
        while (input.read(buffer).also { lengthRead = it } > 0) {
            output.write(buffer, 0, lengthRead)
            output.flush()

            completed(lengthRead)
        }
    }

}