import com.huskerdev.nioex.downloadToFile
import com.huskerdev.nioex.speedometer.DownloadingSpeedometer
import com.huskerdev.nioex.speedometer.ZipSpeedometer
import com.huskerdev.nioex.unzipFilteredToFolder
import com.huskerdev.nioex.unzipToFolder
import java.io.File

fun main(){
    val zip = File("archive.zip")

    // Unzip to folder
    zip.unzipToFolder("folder")

    // Unzip with filter
    zip.unzipFilteredToFolder("folder", {
        return@unzipFilteredToFolder it.name == "requiredFile"
    })

    // Unzipping handler
    zip.unzipToFolder("folder") {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Unzipping entry: ${it.entry}")
        println("Unzipping entry to file: ${it.file}")
    }

    // Unzipping speedometer
    zip.unzipToFolder("folder", ZipSpeedometer {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Unzipping entry: ${it.entry}")
        println("Unzipping entry to file: ${it.file}")
        println("Speed: ${it.bytesPerSecond}")
    })

    // Unzipping speedometer (advanced)
    zip.unzipToFolder("folder", ZipSpeedometer().apply {
        updateDelay = 10
        speedUpdateDelay = 10
        onStarted {
            println("Unzipping started!")
        }
        onCompleted {
            println("Unzipping completed!")
        }
        onUpdate {
            println("-------------------------")
            println("Size: ${it.size}")
            println("Current: ${it.current}")
            println("Unzipping entry: ${it.entry}")
            println("Unzipping entry to file: ${it.file}")
            println("Speed: ${it.bytesPerSecond}")
        }
    })
}