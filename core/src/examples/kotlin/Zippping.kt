import com.huskerdev.nioex.children
import com.huskerdev.nioex.speedometer.ZipSpeedometer
import com.huskerdev.nioex.unzipFilteredToFolder
import com.huskerdev.nioex.unzipToFolder
import com.huskerdev.nioex.zipTo
import java.io.File

fun main(){
    val fileToZip = File("file")

    // Zip to archive
    fileToZip.zipTo("archive.zip")

    // Zip file arrays
    fileToZip.children.zipTo("archive.zip")

    // Zip handler
    fileToZip.zipTo("archive.zip") {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Zipping entry: ${it.entry}")
        println("Zipping file: ${it.file}")
    }

    // Zip speedometer
    fileToZip.zipTo("archive.zip", ZipSpeedometer {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Zipping entry: ${it.entry}")
        println("Zipping file: ${it.file}")
        println("Speed: ${it.bytesPerSecond}")
    })

    // Zip speedometer (advanced)
    fileToZip.zipTo("archive.zip", ZipSpeedometer().apply {
        updateDelay = 10
        speedUpdateDelay = 10
        onStarted {
            println("Zipping started!")
        }
        onCompleted {
            println("Zipping completed!")
        }
        onUpdate {
            println("-------------------------")
            println("Size: ${it.size}")
            println("Current: ${it.current}")
            println("Zipping entry: ${it.entry}")
            println("Zipping file: ${it.file}")
            println("Speed: ${it.bytesPerSecond}")
        }
    })
}