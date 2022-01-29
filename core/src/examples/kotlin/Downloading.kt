
import com.huskerdev.nioex.downloadToFile
import com.huskerdev.nioex.speedometer.DownloadingSpeedometer
import java.net.URL

fun main(){
    val url = URL("https://test.com/image.jpg")

    // Download to file
    url.downloadToFile("image.jpg")

    // Downloading handler
    url.downloadToFile("image.jpg") {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Downloading from: ${it.url}")
        println("Downloading to: ${it.file}")
    }

    // Downloading speedometer
    url.downloadToFile("image.jpg", DownloadingSpeedometer {
        println("-------------------------")
        println("Size: ${it.size}")
        println("Current: ${it.current}")
        println("Downloading from: ${it.url}")
        println("Downloading to: ${it.file}")
        println("Speed: ${it.bytesPerSecond}")
    })

    // Downloading speedometer (advanced)
    url.downloadToFile("image.jpg", DownloadingSpeedometer().apply {
        updateDelay = 10
        speedUpdateDelay = 10
        onStarted {
            println("Downloading started!")
        }
        onCompleted {
            println("Downloading completed!")
        }
        onUpdate {
            println("-------------------------")
            println("Size: ${it.size}")
            println("Current: ${it.current}")
            println("Downloading from: ${it.url}")
            println("Downloading to: ${it.file}")
            println("Speed: ${it.bytesPerSecond}")
        }
    })
}